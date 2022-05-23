package BusinessLayer.Users;

import BusinessLayer.Users.BaseActions.BaseActionType;
import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.ShopController;
import BusinessLayer.Shops.ShopInfo;

import javax.naming.NoPermissionException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserController {

    public AdministratorInfo getMyInfo(String userName, int shopID) {
        if(subscribers.containsKey(userName)){
            SubscribedUser u = subscribers.get(userName);
            ShopAdministrator admin = u.getAdministrator(shopID);
            if(admin != null){
                return admin.getMyInfo();
            }
            return null;
        }
        return null;
    }

    public boolean removeAdmin(int shopID, String requesting, String toRemove) throws NoPermissionException {
        if(subscribers.containsKey(requesting)){
            SubscribedUser u = subscribers.get(requesting);
            return u.removeAdmin(shopID,subscribers.get(toRemove));
        }
        throw new IllegalArgumentException("you aren't a subscribed user!");
    }

    static private class UserControllerHolder {
        static final UserController uc = new UserController();
    }

    private final Map<String, User> users;
    private final Map<String, SubscribedUser> subscribers;
    private final Map<String, SystemManager> managers;
    private int guest_serial = -1;

    public static UserController getInstance() {
        return UserControllerHolder.uc;
    }


    private UserController() {
        users = new ConcurrentHashMap<>();
        managers = new ConcurrentHashMap<>();
        subscribers = new ConcurrentHashMap<>();
    }

    public boolean saveProducts(User u, int shopId, int productId, int quantity) {
        double price = ShopController.getInstance().getProductPrice(shopId,productId);
        if(price != -1) {
            if (u.saveProducts(shopId, productId, quantity,price)) {
                if (!ShopController.getInstance().checkIfUserHasBasket(shopId, u.getName())) {
                    ShopController.getInstance().AddBasket(shopId, u.getName(), u.getBasket(shopId));
                }
                return true;
            }
        }
        return false;
    }

    public ConcurrentHashMap<Integer, Basket> getShoppingCart(String u) {
        User u1 = users.get(u);
        return u1.getShoppingCart();
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
        return u.removeProduct(shopId, productId);
    }

    public boolean editProductQuantity(User u, int shopId, int productId, int newQuantity) {
        return u.editProductQuantity(shopId, productId, newQuantity);
    }


    public ConcurrentHashMap<Integer, ShopInfo> reciveInformation() {
        return ShopController.getInstance().reciveInformation();
    }


    public User getUser(String user) {
        return users.get(user);
    }

    public ConcurrentHashMap<Integer, BasketInfo> showCart(User u) {
        User user = users.get(u.getName());
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
        if (subscribers.containsKey(userNameToAssign)) {
            return currUser.assignShopManager(shopId, getSubUser(userNameToAssign));
        } else
            throw new IllegalArgumentException("non such user - " + userNameToAssign);
    }

    public SubscribedUser getSubUser(String userName) {
        if (!subscribers.containsKey(userName))
            throw new IllegalArgumentException("user " + userName + " doesn't exist");
        return subscribers.getOrDefault(userName, null);
    }

    public SystemManager getSysUser(String userName) {
        if (!managers.containsKey(userName))
            throw new IllegalArgumentException("user " + userName + " doesn't exist or dont have system manager permission");
        return managers.getOrDefault(userName, null);
    }


    public boolean closeShop(SubscribedUser currUser, int shopIdToClose) throws NoPermissionException {
        return currUser.closeShop(shopIdToClose);
    }

    public boolean createSystemManager(String username, String password) {
        SystemManager systemManager = new SystemManager(username, password);
        users.put(systemManager.getName(), systemManager);
        subscribers.put(systemManager.getName(), systemManager);
        managers.put(systemManager.getName(), systemManager);
        return true;
    }

    public synchronized boolean registerToSystem(String userName, String password) {
        if (!subscribers.containsKey(userName)) {
            //todo : change the shoping catr
            SubscribedUser newUser = new SubscribedUser(userName, password);
            users.put(userName, newUser);
            subscribers.put(userName, newUser);
            return true;
        }
        return false;
    }

    public SubscribedUser login(String userName, String password, User currUser) {
        if (subscribers.containsKey(userName) && (currUser == null || currUser instanceof Guest))
            if (subscribers.get(userName).login(userName, password)) {
                if (currUser != null){
                    users.remove(currUser.getUserName());
                }
                return subscribers.get(userName);
            }
            else
                return null;
        return null;
    }

    public Guest logout(String username) {
        if (subscribers.containsKey(username)){
            subscribers.get(username).logout();
            return loginSystem();
        }
        return null;
    } // be a guest now

    public Guest loginSystem() {
        Guest guest = new Guest("guest_" + ++guest_serial);
        users.put(guest.getName(), guest);
        return guest;
    }

    public boolean logoutSystem(String name) {
        return users.remove(name) != null;
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
        for (Integer code:
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

    public Collection<PurchaseHistory> getShopsAndUsersInfo(SystemManager currUser, int shop,String userName)  {
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

    public boolean updateProductQuantity(String username,int shopID, int productID, int newQuantity) throws NoPermissionException {
       ShopAdministrator admin = getAdmin(username, shopID);
       if(admin != null)
           return admin.changeProductQuantity(productID,newQuantity);
       throw new NoPermissionException("you aren't an admin of that shop!");
    }

    public boolean updateProductPrice(String username,int shopID, int productID, double newPrice) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(username, shopID);
        if(admin != null)
            return admin.changeProductPrice(productID,newPrice);
        throw new NoPermissionException("you aren't an admin of that shop!");
    }

    public boolean updateProductDescription(String username,int shopID, int productID, String Desc) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(username, shopID);
        if(admin != null)
            return admin.changeProductDesc(productID,Desc);
        throw new NoPermissionException("you aren't an admin of that shop!");
    }

    public boolean updateProductName(String username,int shopID, int productID, String newName) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(username, shopID);
        if(admin != null)
            return admin.changeProductName(productID,newName);
        throw new NoPermissionException("you aren't an admin of that shop!");
    }

    public boolean deleteProductFromShop(String username,int shopID, int productID) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(username, shopID);
        if(admin != null){
            admin.removeProduct(productID);
            return true;
        }
        throw new NoPermissionException("you aren't an admin of that shop!");
    }

    public boolean addProductToShop(String username, int shopID, String name, String manufacturer, String desc, int productID, int quantity, double price) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(username, shopID);
        if(admin != null){
            admin.addProduct(productID,name,desc,manufacturer,price,quantity);
            return true;
        }
        throw new NoPermissionException("you aren't an admin of that shop!");
    }

    public boolean reopenShop(String userName, int shopID) throws NoPermissionException {
        ShopAdministrator admin = getAdmin(userName,shopID);
        if(admin instanceof ShopOwner){
            ((ShopOwner) admin).reOpenShop();
            return true;
        }
        throw new NoPermissionException("you aren't the founder of that shop!");
    }

    private ShopAdministrator getAdmin(String username, int shopID){
        if (subscribers.containsKey(username)){
            SubscribedUser u = subscribers.get(username);
            ShopAdministrator admin = u.getAdministrator(shopID);
            return admin;
        }
        return null;
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo(SystemManager currUser) {
        return currUser.getShopsAndUsersInfo();
    }

    protected boolean removeSubscribedUserFromSystem(String userName){
        if(!getSubUser(userName).removeFromSystem())
            throw new IllegalArgumentException("user "+userName+" cant be removed");
        return true;
    }
    public enum UserState{
        REMOVED,LOGGED_IN,LOGGED_OUT;

        static UserState get(SubscribedUser u){
            return u.isRemoved()? REMOVED : u.isLoggedIn()? LOGGED_IN:LOGGED_OUT;
        }
        public static int getVal(UserState state){
            return switch (state){
                case REMOVED -> -1;
                case LOGGED_IN -> 1;
                case LOGGED_OUT -> 0;
            };
        }
    }
    public Map<UserState,SubscribedUser> getSubscribedUserInfo(String user){
        if(!getSysUser(user).isLoggedIn())
            throw new IllegalStateException("Mast be logged in for getting userInfo");
        Map<UserState,SubscribedUser> m = new ConcurrentHashMap<>();
        this.subscribers.values().forEach((u)->{m.put(UserState.get(u),u);});
        return m;
    }

    public void clearForTestsOnly() {
        users.clear();
        subscribers.clear();
        managers.clear();
    }
}
