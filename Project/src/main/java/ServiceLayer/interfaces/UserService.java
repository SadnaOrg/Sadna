package main.java.ServiceLayer.interfaces;

import main.java.BusinessLayer.Products.Product;
import main.java.BusinessLayer.Products.ProductFilters;
import main.java.BusinessLayer.Shops.Shop;
import main.java.BusinessLayer.Shops.ShopFilters;
import main.java.BusinessLayer.Shops.ShopInfo;
import main.java.BusinessLayer.Users.BasketInfo;
import main.java.ServiceLayer.Response;
import main.java.ServiceLayer.Result;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface UserService {

    Result loginSystem();

    Result logoutSystem();

    Result registerToSystem(String userName, String password);

    Response<SubscribedUserService> login(String username, String password);

    Result logout(String username);

    Response<ConcurrentHashMap<Integer, ShopInfo>> receiveInformation();

    Response<Map<Shop, Collection<Product>>> searchProducts(ShopFilters shopPred, ProductFilters productPred);

    Result saveProducts(int shopid, int productid, int quantity);

    Response<Map<Integer, BasketInfo>> showCart();

    Result removeProduct(int shopId, int productId);

    Result editProductQuantity(int shopId, int productId, int newQuantity);

    Result purchaseCartFromShop();

}
