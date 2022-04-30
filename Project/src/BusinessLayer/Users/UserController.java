package BusinessLayer.Users;

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

    public ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Integer>> getShoppingCart(User u)
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


    public User getUser(String user)
    {
        return users.get(user);
    }
}
