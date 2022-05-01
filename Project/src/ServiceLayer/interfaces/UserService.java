package ServiceLayer.interfaces;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Shops.ShopFilters;
import BusinessLayer.Shops.ShopInfo;
import BusinessLayer.Users.BasketInfo;
import BusinessLayer.Users.User;
import ServiceLayer.Response;
import ServiceLayer.Result;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface UserService {

    Result loginSystem();

    Result logoutSystem();

    Result registerToSystem(String userName, String password);

    Response<GeneralService> login(String username, String password);

    Response<ConcurrentHashMap<Integer, ShopInfo>> receiveInformation();

    Response<Map<Shop, Collection<Product>>> searchProducts(ShopFilters shopPred, ProductFilters productPred);

    Result saveProducts(int shopid, int productid, int quantity);

    Response<Map<Integer, BasketInfo>> showCart();

    Result removeProduct(int shopId, int productId);

    Result editProductQuantity(int shopId, int productId, int newQuantity);

    Result purchaseCartFromShop();
}
