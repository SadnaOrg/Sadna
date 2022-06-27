package BusinessLayer.Users;

import BusinessLayer.Caches.GuestCache;
import BusinessLayer.Caches.ShopCache;
import BusinessLayer.Caches.SubscribedUserCache;
import BusinessLayer.Caches.SystemManagerCache;
import BusinessLayer.Shops.Polices.Discount.DiscountPred;
import BusinessLayer.Shops.Polices.Discount.DiscountRules;
import BusinessLayer.Shops.Polices.Purchase.PurchasePolicy;
import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.ShopController;
import BusinessLayer.Shops.ShopInfo;
import BusinessLayer.Users.BaseActions.BaseActionType;

import javax.naming.NoPermissionException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserController {



    static private class UserControllerHolder {
        static final UserController uc = new UserController();
    }

    private final GuestCache guests;
    private final SubscribedUserCache subscribers;
    private final SystemManagerCache managers;
    private int guest_serial = -1;

    public static UserController getInstance() {
        return UserControllerHolder.uc;
    }

    private UserController() {
        guests = GuestCache.getInstance();
        managers = SystemManagerCache.getInstance();
        subscribers = SubscribedUserCache.getInstance();
    }

    public AdministratorInfo getMyInfo(String userName, int shopID) {
        SubscribedUser u = getSubUser(userName);
        if(u != null){
            ShopAdministrator admin = u.getAdministrator(shopID);
            if(admin != null){
                return admin.getMyInfo();
            }
            return null;
        }
        return null;
    }

    public boolean removeAdmin(int shopID, String requesting, String toRemove) throws NoPermissionException {
        SubscribedUser remover = getSubUser(requesting);
        SubscribedUser removed;
        if(remover != null){
            removed = getSubUser(toRemove);
            if(removed != null){
                boolean status = remover.removeAdmin(shopID,removed);
                if(status){
                    ShopCache.getInstance().remoteUpdateByID(shopID,remover); // should cascade
                    //subscribers.updateByID(requesting);
                    //subscribers.updateByID(toRemove);
                    return true;
                }
                else return false;
            }
            else throw  new IllegalArgumentException("trying to remove a not subscribed user from the admins of a shop!");
        }
        throw new IllegalArgumentException("you aren't a subscribed user!");
    }

    public Boolean removeShopOwner(int shopID, String requesting, String toRemove) throws NoPermissionException {
        SubscribedUser remover = getSubUser(requesting);
        SubscribedUser removed;
        if(remover != null){
            removed = getSubUser(toRemove);
            if(removed != null){
                boolean status = remover.removeShopOwner(shopID,removed);
                if(status){
                    ShopCache.getInstance().remoteUpdateByID(shopID,remover);// should cascade
                    //subscribers.updateByID(requesting);
                    //subscribers.updateByID(toRemove);
                    return true;
                }
                else return false;
            }
            else throw  new IllegalArgumentException("trying to remove a not subscribed user from the admins of a shop!");
        }
        throw new IllegalArgumentException("you aren't a subscribed user!");
    }

    public boolean saveProducts(User u, int shopId, int productId, int quantity) {
        double price = ShopController.getInstance().getProductPrice(shopId, productId); // inserts shop to cache if needed.
        if (price != -1) {
            if (u.saveProducts(shopId, productId, quantity, price,ShopController.getInstance().getShops().findElement(shopId).getProducts().get(productId).getCategory())) {
                if (!ShopController.getInstance().checkIfUserHasBasket(shopId, u.getName())) {
                    ShopController.getInstance().AddBasket(shopId, u.getName(), u.getBasket(shopId));
                }
                ShopCache.getInstance().mark(shopId); // mark shop as dirty, write new baskets when writing to DB
                if(u instanceof SubscribedUser)
                    subscribers.mark(u.getUserName()); // we only save basket of subscribed user. write changes when logging out
                return true;
            }
        }
        return false;
    }

    public ConcurrentHashMap<Integer, Basket> getShoppingCart(String u) {
        User u1 = getUser(u);
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

    // remove from cart, basket and cart changes aren't written immediately
    public boolean removeproduct(User u, int shopId, int productId) {
        boolean removed = u.removeProduct(shopId, productId);
        if (removed){
            ShopController.getInstance().tryRemove(shopId, u.getUserName(), 0); // inserts shop to cache if needed
            ShopCache.getInstance().mark(shopId);
            if(u instanceof SubscribedUser)// save basket of subscribed user
                subscribers.mark(u.getUserName());
        }
        return removed;
    }

    public boolean editProductQuantity(User u, int shopId, int productId, int newQuantity) {
        boolean edited = u.editProductQuantity(shopId, productId, newQuantity);
        if (edited){
            ShopController.getInstance().tryRemove(shopId, u.getUserName(), newQuantity);// inserts shop to cache if needed
            ShopCache.getInstance().mark(shopId);
            if(u instanceof SubscribedUser) // save basket of subscribed user
                subscribers.mark(u.getUserName());
        }
        return edited;
    }


    public ConcurrentHashMap<Integer, ShopInfo> reciveInformation() {
        return ShopController.getInstance().reciveInformation();
    }


    public User getUser(String user) {
        User u = getSubUser(user);
        if(u == null)
            return guests.findElement(user);
        return u;
    }

    public ConcurrentHashMap<Integer, BasketInfo> showCart(User u) {
        User user = getUser(u.getUserName());
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
        SubscribedUser assignee = getSubUser(userNameToAssign);
        if (assignee != null) {
            boolean status = currUser.assignShopManager(shopId, assignee);
            if(status){
                ShopCache.getInstance().remoteUpdateByID(shopId,currUser); // should cascade changes
                //subscribers.updateElement(currUser.getUserName(), currUser);
                //subscribers.updateByID(userNameToAssign);
            }
            return status;
        } else
            throw new IllegalArgumentException("non such user - " + userNameToAssign);
    }

    public SubscribedUser getSubUser(String userName) {
        SubscribedUser subscribedUser = managers.findElement(userName);
        if (subscribedUser == null)
            subscribedUser = subscribers.findElement(userName);
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
        boolean status = currUser.closeShop(shopIdToClose);
        if(status){
            ShopCache.getInstance().remoteUpdateByID(shopIdToClose, currUser);
        }
        return status;
    }

    public boolean createSystemManager(String username, String password, Date date) {
        SystemManager systemManager = new SystemManager(username, password,date);
        managers.insert(systemManager.getName(), systemManager, true); // write once
        return true;
    }

    public synchronized boolean registerToSystem(String userName, String password, Date date) {
        if (getSubUser(userName) == null) {
            //todo : change the shoping catr
            SubscribedUser newUser = new SubscribedUser(userName, password,date);
            subscribers.insert(userName, newUser, true); // write once
            return true;
        }
        return false;
    }

    public SubscribedUser login(String userName, String password, User currUser) {
        SubscribedUser subscribedUser = getSubUser(userName);
        if ((subscribedUser != null) && (currUser == null || currUser instanceof Guest))
            if (subscribedUser.login(userName, password)) {
                if (currUser != null) {
                    guests.remove(currUser.getUserName());
                }
                return subscribedUser;
            } else
                return null;
        return null;
    }

    public Guest logout(String username) {
        SubscribedUser subscribedUser = getSubUser(username);
        if (subscribedUser != null) {
            subscribedUser.logout();
            if(!managers.remove(username)) // if not a manager he must be a subscribed user, getSubUser put him in the cache
                subscribers.remove(username);
            return loginSystem();
        }
        return null;
    } // be a guest now

    public Guest loginSystem() {
        Guest guest = new Guest("guest_" + ++guest_serial);
        guests.insert(guest.getName(), guest, true);
        return guest;
    }

    public boolean logoutSystem(String name) {
        return guests.remove(name) || subscribers.remove(name) || managers.remove(name);
    } // exit everything

    public boolean assignShopManager(SubscribedUser user, int shop, String userNameToAssign) throws NoPermissionException {
        SubscribedUser assignee = getSubUser(userNameToAssign); // now in cache if wasn't before
        if(assignee == null)
            throw new IllegalArgumentException("there's no user with the assignee name in the system!");
        boolean status = user.assignShopManager(shop, assignee);
        if(status){
            ShopCache.getInstance().remoteUpdateByID(shop, user);
           // subscribers.updateByID(userNameToAssign);
           // subscribers.mark(user.getUserName());
        }
        return status;
    }

    public boolean assignShopOwner(SubscribedUser user, int shop, String userNameToAssign) throws NoPermissionException {
        SubscribedUser assignee = getSubUser(userNameToAssign); // now in cache if wasn't before
        if(assignee == null)
            throw new IllegalArgumentException("there's no user with the assignee name in the system!");
        boolean status = user.assignShopOwner(shop, assignee);
        if(status){
            ShopCache.getInstance().remoteUpdateByID(shop, user);
           // subscribers.updateByID(userNameToAssign);
           // subscribers.mark(user.getUserName());
        }
        return status;
    }

    public boolean changeManagerPermission(SubscribedUser user, int shop, String userNameToAssign, Collection<Integer> types) throws NoPermissionException {
        SubscribedUser assignee = getSubUser(userNameToAssign);
        boolean status = user.changeManagerPermission(shop, assignee, convertToAction(types));
        if(status){
            ShopCache.getInstance().remoteUpdateByID(shop,user); // cascade updates everything
        }
        return true;
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
        if (admin != null){
            boolean status = admin.changeProductQuantity(productID, newQuantity);
            if(status){
                ShopCache.getInstance().remoteUpdateByID(shopID,admin.getSubscribed());
            }
            return status;
        }
        throw new NoPermissionException("you aren't an admin of that shop!");
    }

    public boolean updateProductPrice(String username, int shopID, int productID, double newPrice) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(username, shopID);
        if (admin != null){
            boolean status = admin.changeProductPrice(productID, newPrice);
            if(status){
                ShopCache.getInstance().remoteUpdateByID(shopID,admin.getSubscribed());
            }
            return status;
        }
        throw new NoPermissionException("you aren't an admin of that shop!");
    }

    public boolean updateProductDescription(String username, int shopID, int productID, String Desc) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(username, shopID);
        if (admin != null){
            boolean status = admin.changeProductDesc(productID, Desc);
            if(status){
                ShopCache.getInstance().remoteUpdateByID(shopID,admin.getSubscribed());
            }
            return status;
        }

        throw new NoPermissionException("you aren't an admin of that shop!");
    }

    public boolean updateProductName(String username, int shopID, int productID, String newName) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(username, shopID);
        if (admin != null){
            boolean status = admin.changeProductName(productID, newName);
            if(status){
                ShopCache.getInstance().remoteUpdateByID(shopID,admin.getSubscribed());
            }
            return status;
        }
        throw new NoPermissionException("you aren't an admin of that shop!");
    }

    public boolean deleteProductFromShop(String username, int shopID, int productID) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(username, shopID);
        if (admin != null) {
            admin.removeProduct(productID);
            ShopCache.getInstance().remoteUpdateByID(shopID,admin.getSubscribed());
            return true;
        }
        throw new NoPermissionException("you aren't an admin of that shop!");
    }

    public boolean addProductToShop(String username, int shopID, String name, String manufacturer, String desc, int productID, int quantity, double price) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(username, shopID);
        if (admin != null) {
            admin.addProduct(productID, name, desc, manufacturer, price, quantity);
            ShopCache.getInstance().remoteUpdateByID(shopID,admin.getSubscribed());
            return true;
        }
        throw new NoPermissionException("you aren't an admin of that shop!");
    }

    public boolean reopenShop(String userName, int shopID) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(userName, shopID);
        if (admin instanceof ShopOwner) {
            ((ShopOwner) admin).reOpenShop();
            ShopCache.getInstance().remoteUpdateByID(shopID,admin.getSubscribed());
            return true;
        }
        throw new NoPermissionException("you aren't the founder of that shop!");
    }

    private ShopAdministrator getAdmin(String username, int shopID) {
        SubscribedUser subscribedUser = getSubUser(username);
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
        subscribers.remoteRemove(userName);
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
        boolean status = user.setCategory(productId,category,shopId);
        if(status){
            ShopCache.getInstance().remoteUpdateByID(shopId,user);
        }
        return status;
    }

    public Collection<SystemManager> getSysManagers(){
        return managers.findAll();
    }


    public int createProductByQuantityDiscount(SubscribedUser currUser, int productId, int productQuantity, double discount, int connectId, int shopId) throws NoPermissionException {
        int id = currUser.createProductByQuantityDiscount(productId, productQuantity, discount, connectId, shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }


    public int createProductDiscount(SubscribedUser currUser, int productId, double discount, int connectId, int shopId) throws NoPermissionException {
        int id = currUser.createProductDiscount(productId, discount, connectId, shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }

    public int createProductQuantityInPriceDiscount(SubscribedUser currUser, int productID, int quantity, double priceForQuantity, int connectId, int shopId) throws NoPermissionException {
        int id = currUser.createProductQuantityInPriceDiscount(productID, quantity, priceForQuantity, connectId, shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }

    public int createRelatedGroupDiscount(SubscribedUser currUser, String category, double discount, int connectId , int shopId) throws NoPermissionException {
        int id = currUser.createRelatedGroupDiscount(category, discount, connectId, shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }

    public int createShopDiscount(SubscribedUser currUser, int basketQuantity,double discount,int connectId, int shopId) throws NoPermissionException {
        int id = currUser.createShopDiscount(basketQuantity, discount, connectId, shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }

    public int createDiscountAndPolicy(SubscribedUser currUser, DiscountPred discountPred, DiscountRules discountPolicy, int connectId, int shopId) throws NoPermissionException {
        int id = currUser.createDiscountAndPolicy(discountPred, discountPolicy, connectId, shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }

    public int createDiscountMaxPolicy(SubscribedUser currUser, DiscountRules discountPolicy,int connectId, int shopId) throws NoPermissionException {
        int id = currUser.createDiscountMaxPolicy(discountPolicy, connectId, shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }

    public int  createDiscountOrPolicy(SubscribedUser currUser, DiscountPred discountPred,DiscountRules discountPolicy,int connectId, int shopId) throws NoPermissionException {
        int id = currUser.createDiscountOrPolicy(discountPred, discountPolicy, connectId, shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }

    public int  createDiscountPlusPolicy(SubscribedUser currUser, DiscountRules discountPolicy,int connectId, int shopId) throws NoPermissionException {
        int id = currUser.createDiscountPlusPolicy(discountPolicy, connectId, shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }

    public int createDiscountXorPolicy(SubscribedUser currUser, DiscountRules discountRules1, DiscountRules discountRules2,  DiscountPred tieBreaker,int connectId, int shopId) throws NoPermissionException {
        int id = currUser.createDiscountXorPolicy(discountRules1, discountRules2, tieBreaker, connectId, shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }

    public int  createValidateBasketQuantityDiscount(SubscribedUser currUser, int basketquantity, boolean cantBeMore ,int connectId, int shopId) throws NoPermissionException {
        int id = currUser.createValidateBasketQuantityDiscount(basketquantity, cantBeMore, connectId, shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }

    public int createValidateBasketValueDiscount(SubscribedUser currUser, double basketvalue ,boolean cantBeMore,int connectId, int shopId) throws NoPermissionException {
        int id = currUser.createValidateBasketValueDiscount(basketvalue, cantBeMore, connectId, shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }
    public int createValidateProductQuantityDiscount(SubscribedUser currUser, int productId, int productQuantity, boolean cantbemore ,int connectId, int shopId) throws NoPermissionException {
        int id = currUser.createValidateProductQuantityDiscount(productId, productQuantity, cantbemore, connectId, shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }

    public int createValidateProductPurchase(SubscribedUser currUser,int productId, int productQuantity, boolean cantbemore, int connectId, int shopId) throws NoPermissionException {
        int id = currUser.createValidateProductPurchase(productId, productQuantity, cantbemore, connectId, shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }

    public int createValidateTImeStampPurchase(SubscribedUser currUser,LocalTime localTime, boolean buybefore, int conncectId, int shopId) throws NoPermissionException {
        int id = currUser.createValidateTImeStampPurchase(localTime,buybefore,conncectId,shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }

    public int createValidateDateStampPurchase(SubscribedUser currUser, LocalDate localDate, int conncectId, int shopId) throws NoPermissionException {
        int id = currUser.createValidateDateStampPurchase(localDate, conncectId, shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }

    public int createValidateCategoryPurchase(SubscribedUser currUser,String category, int productQuantity, boolean cantbemore, int connectId, int shopId) throws NoPermissionException {
        int id = currUser.createValidateCategoryPurchase(category, productQuantity, cantbemore, connectId, shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }

    public int createValidateUserPurchase(SubscribedUser currUser,int age, int connectId, int shopId) throws NoPermissionException {
        int id = currUser.createValidateUserPurchase(age,connectId,shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }


    public int createPurchaseAndPolicy(SubscribedUser currUser,PurchasePolicy policy, int conncectId, int shopId) throws NoPermissionException {
        int id = currUser.createPurchaseAndPolicy(policy, conncectId, shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }

    public int createPurchaseOrPolicy(SubscribedUser currUser,PurchasePolicy policy, int conncectId, int shopId) throws NoPermissionException {
        int id = currUser.createPurchaseOrPolicy(policy, conncectId, shopId);
        if(id != -1){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return id;
    }

    public boolean removeDiscount(SubscribedUser currUser,int discountID, int shopId) throws NoPermissionException {
        boolean status = currUser.removeDiscount(discountID,shopId);
        if(status){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return status;
    }

    public boolean removePredicate(SubscribedUser currUser,int predID, int shopId) throws NoPermissionException {
        boolean status = currUser.removePredicate(predID,shopId);
        if(status){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return status;
    }
    public boolean removePurchasePolicy(SubscribedUser currUser,int purchasePolicyToDelete, int shopId) throws NoPermissionException {
        boolean status = currUser.removePurchasePolicy(purchasePolicyToDelete,shopId);
        if(status){
            ShopCache.getInstance().remoteUpdateByID(shopId,currUser);
        }
        return status;
    }
    public DiscountRules getDiscount(SubscribedUser currUser,int shopId) throws NoPermissionException {
        return currUser.getDiscount(shopId);
    }

    public PurchasePolicy getPurchasePolicy(SubscribedUser currUser,int shopId) throws NoPermissionException {
        return currUser.getPurchasePolicy(shopId);
    }

    public void clearForTestsOnly() {
        guests.clear();
        subscribers.clear();
        managers.clear();
    }
}