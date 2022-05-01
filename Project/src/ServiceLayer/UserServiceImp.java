package ServiceLayer;

import BusinessLayer.Shops.ShopController;
import BusinessLayer.Shops.ShopInfo;
import BusinessLayer.Users.BasketInfo;
import BusinessLayer.Users.Guest;
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

    @Override
    public boolean purchaseCartFromShop(User u) {
        ConcurrentHashMap<Integer,Double> prices = shopController.purchaseBasket(u.getName());
        ConcurrentHashMap<Integer,Boolean> paymentSituation= system.pay(prices);
        shopController.addToPurchaseHistory(u.getName(),paymentSituation);
        return true;
    }

    @Override
    public boolean saveProducts(User u, int shopid, int productid, int quantity){
        return UserController.getInstance().saveProducts(u,shopid,productid,quantity);
    }

    @Override
    public ConcurrentHashMap<Integer, BasketInfo> showCart(User u){
        return UserController.getInstance().showCart(u);
    }

    @Override
    public ConcurrentHashMap<Integer, ShopInfo> reciveInformation(){
        return UserController.getInstance().reciveInformation();
    }

    @Override
    public User loginSystem(String name)
    {
        return UserController.getInstance().loginSystem(name);
    }

    @Override
    public User logoutSystem(String name)
    {
        return UserController.getInstance().logoutSystem(name);
    }

    @Override
    public Result registerToSystem(String userName, String password){
            return Result.tryMakeResult(()->userController.registerToSystem(userName,password),"error in register");
    }

}
