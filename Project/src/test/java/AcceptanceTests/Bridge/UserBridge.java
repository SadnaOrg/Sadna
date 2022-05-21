package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;

import java.util.List;

// This is the interface of the options available for all types of users.

public interface UserBridge {

    Guest visit(); // login system

    SubscribedUser login(String guestName,RegistrationInfo info); // do a login, then getUserInfo

    boolean register(String guestname,RegistrationInfo info); // registerToSystem

    boolean exit(String username); // logout system

    List<Shop> getShopsInfo(ShopFilter shopFilter); // searchProducts

    List<ProductInShop> searchShopProducts(int shopID); // searchProducts

    List<ProductInShop> searchProducts(ProductFilter productFilter); // searchProducts

    List<ProductInShop> filterShopProducts(int shopID,ProductFilter productFilter); // searchProducts

    boolean addProductToCart(String username,int shopID,int productID, int quantity); // saveProducts

    ShoppingCart checkCart(String username); // showCart

    boolean updateCart(String username,int productsID, int shopsID,int quantity); // removeProduct, editProductQuantity

    boolean purchaseCart(String username,String creditCard, int CVV, int expirationMonth, int expirationYear); //purchaseCartFromShop

    ProductInShop searchProductInShop(int productID, int shopID); // searchProducts

}
