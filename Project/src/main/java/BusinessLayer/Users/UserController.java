package BusinessLayer.Users;

import BusinessLayer.Caches.SubscribedUserCache;
import BusinessLayer.Caches.SystemManagerCache;
import BusinessLayer.Caches.UserCache;
import BusinessLayer.Mappers.UserMappers.SubscribedUserMapper;
import BusinessLayer.Shops.Polices.Discount.DiscountPred;
import BusinessLayer.Shops.Polices.Discount.DiscountRules;
import BusinessLayer.Shops.Polices.Purchase.PurchasePolicy;
import BusinessLayer.Users.BaseActions.BaseActionType;
import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.ShopController;
import BusinessLayer.Shops.ShopInfo;

import javax.naming.NoPermissionException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserController {


    static private class UserControllerHolder {
        static final UserController uc = new UserController();
    }

    private final UserCache users;
    private final SubscribedUserCache subscribers;
    private final SystemManagerCache managers;
    private int guest_serial = -1;

    public static UserController getInstance() {
        return UserControllerHolder.uc;
    }

    private UserController() {
        users = new UserCache(30);
        managers = new SystemManagerCache(30);
        subscribers = new SubscribedUserCache(30);
    }

    public AdministratorInfo getMyInfo(String userName, int shopID) {
        SubscribedUser u = subscribers.findElement(userName);
        if(u != null){
            ShopAdministrator admin = u.getAdministrator(shopID);
            if(admin != null){
                return admin.getMyInfo();
            }
            return null;
        }
        return null;
    }
    // TODO: check all functions that change state of object and call matching caches.
    public boolean removeAdmin(int shopID, String requesting, String toRemove) throws NoPermissionException {
        SubscribedUser remover = subscribers.findElement(requesting);
        SubscribedUser removed;
        if(remover != null){
            removed = subscribers.findElement(toRemove);
            if(removed != null)
                return remover.removeAdmin(shopID,removed);
            else throw  new IllegalArgumentException("trying to remove a not subscribed user from the admins of a shop!");
        }
        throw new IllegalArgumentException("you aren't a subscribed user!");
    }

    public Boolean removeShopOwner(int shopID, String requesting, String toRemove) throws NoPermissionException {
        SubscribedUser remover = subscribers.findElement(requesting);
        SubscribedUser removed;
        if(remover != null){
            removed = subscribers.findElement(toRemove);
            if(removed != null)
                return remover.removeShopOwner(shopID,removed);
            else throw  new IllegalArgumentException("trying to remove a not subscribed user from the admins of a shop!");
        }
        throw new IllegalArgumentException("you aren't a subscribed user!");
    }

    public boolean saveProducts(User u, int shopId, int productId, int quantity) {
        double price = ShopController.getInstance().getProductPrice(shopId, productId);
        if (price != -1) {
            if (u.saveProducts(shopId, productId, quantity, price,ShopController.getInstance().getShops().get(shopId).getProducts().get(productId).getCategory())) {
                if (!ShopController.getInstance().checkIfUserHasBasket(shopId, u.getName())) {
                    ShopController.getInstance().AddBasket(shopId, u.getName(), u.getBasket(shopId));
                }
                return true;
            }
        }
        return false;
    }

    public ConcurrentHashMap<Integer, Basket> getShoppingCart(String u) {
        User u1 = users.findElement(u);
        if(u1 != null)
            return u1.getShoppingCart();
        throw new IllegalArgumentException("no such user in the system");
    }

    public ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> getShoppingCartClone(User u) {
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> cartClone = new ConcurrentHashMap<>();
        for (int shopId : u.getShoppingCart().keySet()) {
            Basket basketclone = new Basket(u.getShoppingCart().get(shopId));
            cartClone.put(shopId, basketclone.getProducts());
        }
        return cartClone;
    }

    public boolean removeproduct(User u, int shopId, int productId) {
        boolean removed = u.removeProduct(shopId, productId);
        if (removed)
            ShopController.getInstance().tryRemove(shopId, u.getUserName(), 0);
        return removed;
    }

    public boolean editProductQuantity(User u, int shopId, int productId, int newQuantity) {
        boolean edited = u.editProductQuantity(shopId, productId, newQuantity);
        if (edited)
            ShopController.getInstance().tryRemove(shopId, u.getUserName(), newQuantity);
        return edited;
    }


    public ConcurrentHashMap<Integer, ShopInfo> reciveInformation() {
        return ShopController.getInstance().reciveInformation();
    }


    public User getUser(String user) {
        return users.findElement(user);
    }

    public ConcurrentHashMap<Integer, BasketInfo> showCart(User u) {
        User user = users.findElement(u.getUserName());
        if(user == null)
            throw new IllegalArgumentException("no such user in the system");
        return user.showCart();
    }


    /**
     * @param currUser         the current shopAdministrator
     * @param userNameToAssign the user to assign to be a shop manager
     * @param shopId           the shop id
     * @return true if the transaction succeeds , false if not
     * @throws NoPermissionException if the shop Administrator has no permission to complete the transaction
     */
    public boolean AssignShopManager(SubscribedUser currUser, String userNameToAssign, int shopId) throws NoPermissionException {
        if (subscribers.findElement(userNameToAssign) != null) {
            return currUser.assignShopManager(shopId, getSubUser(userNameToAssign));
        } else
            throw new IllegalArgumentException("non such user - " + userNameToAssign);
    }

    public SubscribedUser getSubUser(String userName) {
        SubscribedUser subscribedUser = subscribers.findElement(userName);
        if (subscribedUser == null)
            throw new IllegalArgumentException("user " + userName + " doesn't exist");
        return subscribedUser;
    }

    public SystemManager getSysUser(String userName) {
        SystemManager systemManager = managers.findElement(userName);
        if (systemManager == null)
            throw new IllegalArgumentException("user " + userName + " doesn't exist or dont have system manager permission");
        return systemManager;
    }


    public boolean closeShop(SubscribedUser currUser, int shopIdToClose) throws NoPermissionException {
        return currUser.closeShop(shopIdToClose);
    }

    public boolean createSystemManager(String username, String password, Date date) {
        SystemManager systemManager = new SystemManager(username, password,date);
        users.insert(systemManager.getName(), systemManager, true);
        subscribers.insert(systemManager.getName(), systemManager, true);
        managers.insert(systemManager.getName(), systemManager, true);
        return true;
    }

    public synchronized boolean registerToSystem(String userName, String password, Date date) {
        if (subscribers.findElement(userName) == null) {
            //todo : change the shoping catr
            SubscribedUser newUser = new SubscribedUser(userName, password,date);
            users.insert(userName, newUser, false);
            subscribers.insert(userName, newUser, false);
            return true;
        }
        return false;
    }

    public SubscribedUser login(String userName, String password, User currUser) {
        SubscribedUser subscribedUser = subscribers.findElement(userName);
        if ((subscribedUser != null) && (currUser == null || currUser instanceof Guest))
            if (subscribedUser.login(userName, password)) {
                if (currUser != null) {
                    users.remove(currUser.getUserName(),false);
                }
                return subscribedUser;
            } else
                return null;
        return null;
    }

    public Guest logout(String username) {
        SubscribedUser subscribedUser = subscribers.findElement(username);
        if (subscribedUser != null) {
            subscribedUser.logout();
            subscribers.remove(username,false);
            return loginSystem();
        }
        return null;
    } // be a guest now

    public Guest loginSystem() {
        Guest guest = new Guest("guest_" + ++guest_serial);
        users.insert(guest.getName(), guest, true);
        return guest;
    }

    public boolean logoutSystem(String name) {
        return users.remove(name, false);
    } // exit everything

    public boolean assignShopManager(SubscribedUser user, int shop, String userNameToAssign) throws NoPermissionException {
        return user.assignShopManager(shop, getSubUser(userNameToAssign));
    }

    public boolean assignShopOwner(SubscribedUser user, int shop, String userNameToAssign) throws NoPermissionException {
        return user.assignShopOwner(shop, getSubUser(userNameToAssign));
    }

    public boolean changeManagerPermission(SubscribedUser user, int shop, String userNameToAssign, Collection<Integer> types) throws NoPermissionException {
        return user.changeManagerPermission(shop, getSubUser(userNameToAssign), convertToAction(types));
    }

    private Collection<BaseActionType> convertToAction(Collection<Integer> types) {
        List<BaseActionType> actionTypes = new LinkedList<>();
        for (Integer code :
                types) {
            actionTypes.add(BaseActionType.lookup(code));
        }
        return actionTypes;
    }

    public Collection<AdministratorInfo> getAdministratorInfo(SubscribedUser currUser, int shop) throws NoPermissionException {
        return currUser.getAdministratorInfo(shop);
    }

    public Collection<PurchaseHistory> getHistoryInfo(SubscribedUser currUser, int shop) throws NoPermissionException {
        return currUser.getHistoryInfo(shop);
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo(SystemManager currUser, int shop, String userName) {
        return currUser.getShopsAndUsersInfo(shop, userName);
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo(SystemManager currUser, String userName) {
        return currUser.getShopsAndUsersInfo(userName);
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo(SystemManager currUser, int shop) {
        return currUser.getShopsAndUsersInfo(shop);
    }

    public boolean removeSubscribedUserFromSystem(SystemManager currUser, String userToRemoved) {
        return currUser.removeSubscribedUser(userToRemoved);
    }

    public boolean updateProductQuantity(String username, int shopID, int productID, int newQuantity) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(username, shopID);
        if (admin != null)
            return admin.changeProductQuantity(productID, newQuantity);
        throw new NoPermissionException("you aren't an admin of that shop!");
    }

    public boolean updateProductPrice(String username, int shopID, int productID, double newPrice) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(username, shopID);
        if (admin != null)
            return admin.changeProductPrice(productID, newPrice);
        throw new NoPermissionException("you aren't an admin of that shop!");
    }

    public boolean updateProductDescription(String username, int shopID, int productID, String Desc) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(username, shopID);
        if (admin != null)
            return admin.changeProductDesc(productID, Desc);
        throw new NoPermissionException("you aren't an admin of that shop!");
    }

    public boolean updateProductName(String username, int shopID, int productID, String newName) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(username, shopID);
        if (admin != null)
            return admin.changeProductName(productID, newName);
        throw new NoPermissionException("you aren't an admin of that shop!");
    }

    public boolean deleteProductFromShop(String username, int shopID, int productID) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(username, shopID);
        if (admin != null) {
            admin.removeProduct(productID);
            return true;
        }
        throw new NoPermissionException("you aren't an admin of that shop!");
    }

    public boolean addProductToShop(String username, int shopID, String name, String manufacturer, String desc, int productID, int quantity, double price) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(username, shopID);
        if (admin != null) {
            admin.addProduct(productID, name, desc, manufacturer, price, quantity);
            return true;
        }
        throw new NoPermissionException("you aren't an admin of that shop!");
    }

    public boolean reopenShop(String userName, int shopID) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(userName, shopID);
        if (admin instanceof ShopOwner) {
            ((ShopOwner) admin).reOpenShop();
            return true;
        }
        throw new NoPermissionException("you aren't the founder of that shop!");
    }

    private ShopAdministrator getAdmin(String username, int shopID) {
        SubscribedUser subscribedUser = subscribers.findElement(username);
        if (subscribedUser != null) {
            return subscribedUser.getAdministrator(shopID);
        }
        return null;
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo(SystemManager currUser) {
        return currUser.getShopsAndUsersInfo();
    }

    protected boolean removeSubscribedUserFromSystem(String userName){
        if(!getSubUser(userName).removeFromSystem())
            throw new IllegalArgumentException("user "+userName+" cant be removed");
        subscribers.remove(userName, false);
        users.remove(userName, false);
        System.out.println("renoved ----------------> " +userName);
        return true;
    }



    public enum UserState {
        REMOVED, LOGGED_IN, LOGGED_OUT;

        static UserState get(SubscribedUser u) {
            return u.isRemoved() ? REMOVED : u.isLoggedIn() ? LOGGED_IN : LOGGED_OUT;
        }

        public static int getVal(UserState state) {
            return switch (state) {
                case REMOVED -> -1;
                case LOGGED_IN -> 1;
                case LOGGED_OUT -> 0;
            };
        }
    }

    public Map<UserState,List<SubscribedUser>> getSubscribedUserInfo(String user){
        if(!getSysUser(user).isLoggedIn())
            throw new IllegalStateException("Mast be logged in for getting userInfo");
        Map<UserState,List<SubscribedUser>> m = new ConcurrentHashMap<>();
        this.subscribers.findAll().forEach((u)->{
            var k =UserState.get(u);
            if(!m.containsKey(k))
                m.put(k,new LinkedList<SubscribedUser>());
            m.get(k).add(u);});
        return m;
    }

    public boolean setCategory(SubscribedUser user,int productId, String category, int shopId) throws NoPermissionException {
        return user.setCategory(productId,category,shopId);
    }

    public Collection<SystemManager> getSysManagers(){
        return managers.findAll();
    }


    public int createProductByQuantityDiscount(SubscribedUser currUser, int productId, int productQuantity, double discount, int connectId, int shopId) throws NoPermissionException {
        return currUser.createProductByQuantityDiscount(productId, productQuantity, discount, connectId, shopId);
    }


    public int createProductDiscount(SubscribedUser currUser, int productId, double discount, int connectId, int shopId) throws NoPermissionException {
        return currUser.createProductDiscount(productId, discount, connectId, shopId);
    }

    public int createProductQuantityInPriceDiscount(SubscribedUser currUser, int productID, int quantity, double priceForQuantity, int connectId, int shopId) throws NoPermissionException {
        return currUser.createProductQuantityInPriceDiscount(productID, quantity, priceForQuantity, connectId, shopId);
    }

    public int createRelatedGroupDiscount(SubscribedUser currUser, String category, double discount, int connectId , int shopId) throws NoPermissionException {
        return currUser.createRelatedGroupDiscount(category, discount, connectId, shopId);
    }

    public int createShopDiscount(SubscribedUser currUser, int basketQuantity,double discount,int connectId, int shopId) throws NoPermissionException {

        return currUser.createShopDiscount(basketQuantity, discount, connectId, shopId);
    }

    public int createDiscountAndPolicy(SubscribedUser currUser, DiscountPred discountPred, DiscountRules discountPolicy, int connectId, int shopId) throws NoPermissionException {
        return currUser.createDiscountAndPolicy(discountPred, discountPolicy, connectId, shopId);
    }

    public int createDiscountMaxPolicy(SubscribedUser currUser, DiscountRules discountPolicy,int connectId, int shopId) throws NoPermissionException {
        return currUser.createDiscountMaxPolicy(discountPolicy, connectId, shopId);
    }

    public int  createDiscountOrPolicy(SubscribedUser currUser, DiscountPred discountPred,DiscountRules discountPolicy,int connectId, int shopId) throws NoPermissionException {
        return currUser.createDiscountOrPolicy(discountPred, discountPolicy, connectId, shopId);
    }

    public int  createDiscountPlusPolicy(SubscribedUser currUser, DiscountRules discountPolicy,int connectId, int shopId) throws NoPermissionException {
        return currUser.createDiscountPlusPolicy(discountPolicy, connectId, shopId);
    }

    public int createDiscountXorPolicy(SubscribedUser currUser, DiscountRules discountRules1, DiscountRules discountRules2,  DiscountPred tieBreaker,int connectId, int shopId) throws NoPermissionException {
        return currUser.createDiscountXorPolicy(discountRules1, discountRules2, tieBreaker, connectId, shopId);
    }

    public int  createValidateBasketQuantityDiscount(SubscribedUser currUser, int basketquantity, boolean cantBeMore ,int connectId, int shopId) throws NoPermissionException {
        return currUser.createValidateBasketQuantityDiscount(basketquantity, cantBeMore, connectId, shopId);
    }

    public int createValidateBasketValueDiscount(SubscribedUser currUser, double basketvalue ,boolean cantBeMore,int connectId, int shopId) throws NoPermissionException {
        return currUser.createValidateBasketValueDiscount(basketvalue, cantBeMore, connectId, shopId);
    }
    public int createValidateProductQuantityDiscount(SubscribedUser currUser, int productId, int productQuantity, boolean cantbemore ,int connectId, int shopId) throws NoPermissionException {

        return currUser.createValidateProductQuantityDiscount(productId, productQuantity, cantbemore, connectId, shopId);
    }

    public int createValidateProductPurchase(SubscribedUser currUser,int productId, int productQuantity, boolean cantbemore, int connectId, int shopId) throws NoPermissionException {
        return currUser.createValidateProductPurchase(productId, productQuantity, cantbemore, connectId, shopId);
    }

    public int createValidateTImeStampPurchase(SubscribedUser currUser,LocalTime localTime, boolean buybefore, int conncectId, int shopId) throws NoPermissionException {
        return currUser.createValidateTImeStampPurchase(localTime,buybefore,conncectId,shopId);
    }

    public int createValidateDateStampPurchase(SubscribedUser currUser, LocalDate localDate, int conncectId, int shopId) throws NoPermissionException {
        return currUser.createValidateDateStampPurchase(localDate, conncectId, shopId);
    }

    public int createValidateCategoryPurchase(SubscribedUser currUser,String category, int productQuantity, boolean cantbemore, int connectId, int shopId) throws NoPermissionException {
        return currUser.createValidateCategoryPurchase(category, productQuantity, cantbemore, connectId, shopId);
    }

    public int createValidateUserPurchase(SubscribedUser currUser,int age, int connectId, int shopId) throws NoPermissionException {
        return currUser.createValidateUserPurchase(age,connectId,shopId);
    }


    public int createPurchaseAndPolicy(SubscribedUser currUser,PurchasePolicy policy, int conncectId, int shopId) throws NoPermissionException {
        return currUser.createPurchaseAndPolicy(policy, conncectId, shopId);
    }

    public int createPurchaseOrPolicy(SubscribedUser currUser,PurchasePolicy policy, int conncectId, int shopId) throws NoPermissionException {
        return currUser.createPurchaseOrPolicy(policy, conncectId, shopId);
    }

    public boolean removeDiscount(SubscribedUser currUser,int discountID, int shopId) throws NoPermissionException {
        return currUser.removeDiscount(discountID,shopId);
    }

    public boolean removePredicate(SubscribedUser currUser,int predID, int shopId) throws NoPermissionException {
        return currUser.removePredicate(predID,shopId);
    }
    public boolean removePurchasePolicy(SubscribedUser currUser,int purchasePolicyToDelete, int shopId) throws NoPermissionException {
        return currUser.removePurchasePolicy(purchasePolicyToDelete,shopId);
    }
    public DiscountRules getDiscount(SubscribedUser currUser,int shopId) throws NoPermissionException {
        return currUser.getDiscount(shopId);
    }

    public PurchasePolicy getPurchasePolicy(SubscribedUser currUser,int shopId) throws NoPermissionException {
        return currUser.getPurchasePolicy(shopId);
    }
}