package ServiceLayer.interfaces;

import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Shops.ShopFilters;
import ServiceLayer.Objects.*;
import ServiceLayer.Response;
import ServiceLayer.Result;

public interface UserService {

    Result loginSystem();

    Result logoutSystem();

    Result registerToSystem(String userName, String password);

    Response<SubscribedUserService> login(String username, String password);

    Response<ShopsInfo> receiveInformation();

    Response<ShopsInfo> searchProducts(ShopFilters shopPred, ProductFilters productPred);

    Result purchaseCartFromShop(String creditCardNumber, int CVV, int expiryMonth, int expiryYear);

    Result saveProducts(int shopid, int productid, int quantity);

    Response<Cart> showCart();

    Result removeProduct(int shopId, int productId);

    Result editProductQuantity(int shopId, int productId, int newQuantity);

    Response<User> getUserInfo();

}
