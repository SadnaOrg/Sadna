package ServiceLayer.interfaces;

import BusinessLayer.Notifications.Notification;
import ServiceLayer.Objects.*;
import ServiceLayer.Response;
import ServiceLayer.Result;

import java.util.function.Function;
import java.util.function.Predicate;

public interface UserService {

    Result loginSystem();

    Result logoutSystem();

    Result registerToSystem(String userName, String password);

    Response<SubscribedUserService> login(String username, String password);

    Response<ShopsInfo> receiveInformation();

    Result purchaseCartFromShop(String creditCardNumber, int CVV, int expiryMonth, int expiryYear);

    Result saveProducts(int shopid, int productid, int quantity);

    Response<Cart> showCart();

    Result removeProduct(int shopId, int productId);

    Result editProductQuantity(int shopId, int productId, int newQuantity);

    Response<ShopsInfo> searchProducts(Predicate<Shop> shopPred, Predicate<Product> productPred);

    Response<User> getUserInfo();


    Result registerToNotifier(Function<ServiceLayer.Objects.Notification, Boolean> con);

    Result getDelayNotification();
}
