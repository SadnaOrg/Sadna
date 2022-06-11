package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;

import java.util.List;

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

}
