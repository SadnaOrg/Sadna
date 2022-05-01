package ServiceLayer.interfaces;

import BusinessLayer.Shops.ShopInfo;
import BusinessLayer.Users.BasketInfo;
import BusinessLayer.Users.User;
import ServiceLayer.Result;

import java.util.concurrent.ConcurrentHashMap;

public interface UserService {
    boolean purchaseCartFromShop(User u);

    boolean saveProducts(User u, int shopid, int productid, int quantity);

    ConcurrentHashMap<Integer, BasketInfo> showCart(User u);

    ConcurrentHashMap<Integer, ShopInfo> reciveInformation();

    User loginSystem(String name);

    User logoutSystem(String name);

    Result registerToSystem(String userName, String password);
}
