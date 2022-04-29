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

    public boolean purchaseCart(String user) {
        User u = new User() {};
        if (users.containsKey(user)) {
            u = users.get(user);
        }

        for(int shopid:u.getShoppingCart().keySet()) {
            ConcurrentHashMap<Integer,Integer> basket = u.purchaseBasket(shopid);
//            int price = shopcontroller.purchaseBasket(basket);
//            if(price>0)
//            {
//                pay
//            }
//            else
//            {
//                //problem
//            }
        }
        return true;
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
