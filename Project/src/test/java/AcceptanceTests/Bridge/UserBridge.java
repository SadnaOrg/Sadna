package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;

import java.util.List;

// This is the interface of the options available for all types of users.

public interface UserBridge {

    Guest visit(); // login system

    SubscribedUser login(int guestID, RegistrationInfo info); // use getUserName in the adapter

    SubscribedUser register(int guestID, RegistrationInfo info); // registerToSystem

    boolean exit(int userID); // logout system

    List<Shop> getShopsInfo(ShopFilter shopFilter); // searchProducts

    List<ProductInShop> searchShopProducts(int shopID); // searchProducts

    List<ProductInShop> searchProducts(ProductFilter productFilter); // searchProducts

    List<ProductInShop> filterShopProducts(int shopID,ProductFilter productFilter); // searchProducts

    boolean addProductToCart(int userID,int shopID,int productID, int quantity); // saveProducts

    ShoppingCart checkCart(int userID); // showCart

    boolean updateCart(int userID,int[] productsIDS, int[] shopsIDS,int[] quantities); // removeProduct, editProductQuantity

    boolean purchaseCart(int userID); //purchaseCartFromShop

    ProductInShop searchProductInShop(int productID, int shopID); // searchProducts
}
