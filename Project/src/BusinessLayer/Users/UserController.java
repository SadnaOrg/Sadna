package BusinessLayer.Users;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserController {
    private final Map<String,User> users;
    public UserController() {
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

}
