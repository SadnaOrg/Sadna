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

    private final Map<String,User> users;

    private UserController() {
        users = new ConcurrentHashMap<>();
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

    public void openShop(SubscribedUser su, String name) {
        ShopController.getInstance().openShop(su, name);
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
        if(users.containsKey(userNameToAssign)&&getUser(userNameToAssign)instanceof SubscribedUser){
            return currUser.assignShopManager(shopId,(SubscribedUser) getUser(userNameToAssign));
        }else
            throw new IllegalArgumentException("non such user - "+userNameToAssign);
    }


    public boolean closeShop(SubscribedUser currUser, int shopIdToClose) throws NoPermissionException {
        return currUser.closeShop(shopIdToClose);
    }


    public boolean createSystemManager(String username,String password) {
        User systemManager = new SystemManager(username,password);
        users.put(systemManager.getName(),systemManager);
        return true;
    }

    public boolean registerToSystem(String userName, String password){
        if (users.containsKey(userName)) {
            SubscribedUser newUser = new SubscribedUser(userName, password);
            users.put(userName, newUser);
            return true;
        }
        return false;
    }

    public User loginSystem(String name) {
        User guest= new Guest(name);
        users.put(guest.getName(),guest);
        return guest;
    }

    public User logoutSystem(String name) {
        return users.remove(name);
    }

}
