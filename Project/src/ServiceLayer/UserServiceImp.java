package ServiceLayer;

import BusinessLayer.Shops.ShopController;
import BusinessLayer.Users.User;
import BusinessLayer.Users.UserController;
import ServiceLayer.interfaces.UserService;

import java.util.concurrent.ConcurrentHashMap;

public class UserServiceImp implements UserService {
    UserController userController = UserController.getInstance();
    ShopController shopController = ShopController.getInstance();

    public boolean purchaseCart(String user) {
        User u = userController.getUser(user);
        for(int shopid:userController.getShoppingCart(u).keySet()) {
            ConcurrentHashMap<Integer,Integer> basket = u.purchaseBasket(shopid);
            double price = shopController.purchaseBasket(shopid, basket);
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
