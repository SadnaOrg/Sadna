package BusinessLayer.Users;

import BusinessLayer.Shops.ShopController;
import BusinessLayer.Shops.ShopInfo;

import javax.naming.NoPermissionException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserController {

    static private class UserControllerHolder {
        static final UserController uc = new UserController();
    }

    public static UserController getInstance(){
        return UserControllerHolder.uc;
    }
    private  int guest_serial=-1;
    private final Map<String,User> users;
    private final Map<String,SubscribedUser> subscribers;
    private final Map<String,SystemManager> managers;


    private UserController() {
        users = new ConcurrentHashMap<>();
        managers = new ConcurrentHashMap<>();
        subscribers = new ConcurrentHashMap<>();
    }

    public boolean saveProducts(User u ,int shopId, int productId, int quantity){
        if(u.saveProducts(shopId, productId, quantity))
        {
            if(!ShopController.getInstance().checkIfUserHasBasket(shopId,u.getName())) {
                ShopController.getInstance().AddBasket(shopId,u.getName(), u.getBasket(shopId));
            }
        }
        return true;
    }

    public ConcurrentHashMap<Integer, Basket> getShoppingCart(String u)
    {
        return users.get(u).getShoppingCart();
    }

    public ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Integer>> getShoppingCartClone(User u){
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Integer>> cartClone = new ConcurrentHashMap<>();
        for (int shopId:u.getShoppingCart().keySet())
        {
            Basket basketclone = new Basket(u.getShoppingCart().get(shopId));
            cartClone.put(shopId,basketclone.getProducts());
        }
        return cartClone;
    }

    public boolean removeproduct(User u, int shopId, int productId){
        return u.removeproduct(shopId,productId);
    }

    public boolean editProductQuantity(User u, int shopId, int productId, int newQuantity){
        return u.editProductQuantity(shopId, productId, newQuantity);
    }


    public ConcurrentHashMap<Integer, ShopInfo> reciveInformation(){
        return ShopController.getInstance().reciveInformation();
    }


    public User getUser(String user){
        return users.get(user);
    }

    public ConcurrentHashMap<Integer,BasketInfo> showCart(User u){
        return users.get(u.getName()).showCart();
    }



    /**
     *
     * @param currUser  the current shopAdministrator
     * @param userNameToAssign the user to assign to be a shop manager
     * @param shopId the shop id
     * @return true if the transaction succeeds , false if not
     * @throws NoPermissionException  if the shop Administrator has no permission to complete the transaction
     */
    public boolean AssignShopManager(SubscribedUser currUser,String userNameToAssign,int shopId) throws NoPermissionException {
        if(subscribers.containsKey(userNameToAssign)){
            return currUser.assignShopManager(shopId, getSubUser(userNameToAssign));
        }else
            throw new IllegalArgumentException("non such user - "+userNameToAssign);
    }

    private SubscribedUser getSubUser(String userName) {
        return subscribers.getOrDefault(userName,null);
    }


    public boolean closeShop(SubscribedUser currUser, int shopIdToClose) throws NoPermissionException {
        return currUser.closeShop(shopIdToClose);
    }


    public boolean createSystemManager(String username,String password) {
        SystemManager systemManager = new SystemManager(username,password);
        users.put(systemManager.getName(),systemManager);
        subscribers.put(systemManager.getName(),systemManager);
        managers.put(systemManager.getName(),systemManager);
        return true;
    }

    public synchronized boolean registerToSystem(String userName, String password){
        if (subscribers.containsKey(userName)){
            //todo : change the shoping catr
            SubscribedUser newUser = new SubscribedUser(userName, password);
            users.put(userName, newUser);
            subscribers.put(userName, newUser);
            return true;
        }
        return false;
    }

    public SubscribedUser login(String userName, String password,User currUser){
            if(subscribers.containsKey(userName) &&(currUser==null||currUser instanceof Guest))
                if(subscribers.get(userName).login(userName,password))
                    return subscribers.get(userName);
                else
                    return null;
            return null;
    }

    public void logout(SubscribedUser u){
       u.logout();
    }

    public User loginSystem() {
        User guest= new Guest("guest_" + ++guest_serial);
        users.put(guest.getName(),guest);
        return guest;
    }

    public boolean logoutSystem(String name) {
        return users.remove(name)!=null;
    }

}
