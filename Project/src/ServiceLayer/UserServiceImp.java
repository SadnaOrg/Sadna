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

    public boolean purchaseCartFromShop(User u) {
        ConcurrentHashMap<Integer,Double> prices = shopController.purchaseBasket(u.getName());
        ConcurrentHashMap<Integer,Boolean> paymentSituation= system.pay(prices);
        shopController.addToPurchaseHistory(u.getName(),paymentSituation);
        return true;
    }


}
