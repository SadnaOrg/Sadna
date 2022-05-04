package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;

import java.util.List;

// This is the interface of the options available for all types of users.

public interface UserBridge {

    Guest visit();

    SubscribedUser register(int guestID, RegistrationInfo info);

    boolean exit(int userID);

    List<Shop> getShopsInfo(ShopFilter shopFilter);

    List<ProductInShop> searchShopProducts(int shopID);

    List<ProductInShop> searchProducts(ProductFilter productFilter);

    List<ProductInShop> filterShopProducts(int shopID,ProductFilter productFilter);

    boolean addProductToCart(int userID,int shopID,int productID, int quantity);

    ShoppingCart checkCart(int userID);

    boolean updateCart(int userID,int[] productsIDS, int[] shopsIDS,int[] quantities);

    boolean purchaseCart(int userID);

    ProductInShop searchProductInShop(int productID, int shopID);
}
