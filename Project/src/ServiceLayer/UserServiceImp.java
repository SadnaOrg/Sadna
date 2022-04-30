package ServiceLayer;

import BusinessLayer.Shops.ShopController;
import BusinessLayer.Users.User;
import BusinessLayer.Users.UserController;
import ServiceLayer.interfaces.UserService;
import BusinessLayer.System.System;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class UserServiceImp implements UserService {
    UserController userController = UserController.getInstance();
    ShopController shopController = ShopController.getInstance();
    System system = System.getInstance();

    public ConcurrentHashMap<Integer,Boolean> purchaseCartfromshop(User u) {
        ConcurrentHashMap<Integer,Double> prices = shopController.purchaseBasket(userController.getShoppingCart(u));
        ConcurrentHashMap<Integer,Boolean> paymentSituation= system.pay(prices);
        return paymentSituation;
    }


}
