package BusinessLayer.Users;

import javax.naming.NoPermissionException;
import BusinessLayer.Shops.ShopController;

import BusinessLayer.Shops.ShopController;
import BusinessLayer.Shops.ShopInfo;

import java.util.HashMap;
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

    public boolean saveProducts(User u ,int shopid, int productid, int quantity)
    {
        if(u.saveProducts(shopid, productid, quantity))
        {
            if(!ShopController.getInstance().checkIfUserHasBasket(shopid,u.getName())) {
                ShopController.getInstance().AddBasket(shopid,u.getName(), u.getBasket(shopid));
            }
        }
        return true;
    }

    public ConcurrentHashMap<Integer, Basket> getShoppingCart(String u)
    {
        return users.get(u).getShoppingCart();
    }

    public ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Integer>> getShoppingCartClone(User u)
    {
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Integer>> cartClone = new ConcurrentHashMap<>();
        for (int shopid:u.getShoppingCart().keySet())
        {
            Basket basketclone = new Basket(u.getShoppingCart().get(shopid));
            cartClone.put(shopid,basketclone.getProducts());
        }
        return cartClone;
    }

    public boolean removeproduct(User u, int shopid, int productid)
    {
        return u.removeproduct(shopid,productid);
    }

    public boolean editProductQuantity(User u, int shopid, int productid, int newquantity)
    {
        return u.editProductQuantity(shopid, productid, newquantity);
    }


    public ConcurrentHashMap<Integer, ShopInfo> reciveInformation()
    {
        return ShopController.getInstance().reciveInformation();
    }


    public User getUser(String user)
    {
        return users.get(user);
    }


    /**
     *
     * @param currUser  the current shopAdministrator
     * @param userNameToAssign the user to assign to be a shop manager
     * @param shopID the shop id
     * @return true if the transaction succeeds , false if not
     * @throws NoPermissionException  if the shop Administrator has no permission to complete the transaction
     */
    public boolean AssignShopManager(SubscribedUser currUser,String userNameToAssign,int shopID) throws NoPermissionException {
        if(users.containsKey(userNameToAssign)&&getUser(userNameToAssign)instanceof SubscribedUser){
            return currUser.assignShopManager(shopID,(SubscribedUser) getUser(userNameToAssign));
        }else
            throw new IllegalArgumentException("non such user - "+userNameToAssign);
    }
}
