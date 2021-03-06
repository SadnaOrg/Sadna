package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;
import ServiceLayer.Result;

import java.util.List;
import java.util.function.Function;

// This is the interface of the options available for all types of users.

public interface UserBridge {

    Guest visit();

    SubscribedUser login(String guestName,RegistrationInfo info);

    boolean register(String guestname,RegistrationInfo info);

    boolean exit(String username);

    List<Shop> getShopsInfo(String username,ShopFilter shopFilter);

    List<ProductInShop> searchShopProducts(String username,int shopID);

    List<ProductInShop> searchProducts(String username,ProductFilter productFilter);

    List<ProductInShop> filterShopProducts(String username,int shopID,ProductFilter productFilter);

    boolean addProductToCart(String username,int shopID,int productID, int quantity);

    ShoppingCart checkCart(String username);

    boolean updateCart(String username,int productsID, int shopsID,int quantity);

    double purchaseCart(String username,String creditCard, int CVV, int expirationMonth, int expirationYear);

    ProductInShop searchProductInShop(String username,int productID, int shopID);

    boolean registerToNotifier(String username);

    List<AcceptanceTests.DataObjects.Notification> getDelayNotification(String username);

    List<Notification> getNotifications(String username);

    boolean saveProductsAsBid(String username,int shopId, int productId, int quantity, double price);

}
