package BusinessLayer.Users;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserController {
    private final Map<String,User> users;

    public UserController() {
        users = new ConcurrentHashMap<>();
    }


    public ConcurrentHashMap<Integer, Basket> getShoppingCart(User u)
    {
        ConcurrentHashMap<Integer, Basket> cartClone = new ConcurrentHashMap<>();
        for (int shopid:u.getShoppingCart().keySet())
        {
            Basket basketclone = new Basket(u.getShoppingCart().get(shopid));
            cartClone.put(shopid,basketclone);
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


}
