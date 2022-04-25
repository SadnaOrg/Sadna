package test.Bridge;

import test.Mocks.*;

import java.util.List;

// This is the interface of the options available for all types of users.

public interface UserBridge {

    Guest visit();

    SubscribedUser login(Guest guest,String username,String password);

    SubscribedUser register(Guest guest,RegistrationInfo info);

    Guest logout(SubscribedUser user);

    boolean exit(User user);

    List<Shop> getShopsInfo(ShopFilter shopFilter);

    List<Product> searchShopProducts(Shop shop);

    List<Product> searchProducts(ProductFilter productFilter);

    List<ProductInShop> filterShopProducts(int shopID,ProductFilter productFilter);

    ShoppingCart addProductToCart(int userID,int shopID,int productID, int quantity);

    ShoppingCart checkCart(int userID);

    ShoppingCart updateCart(int userID,int[] productsIDS, int[] shopsIDS,int[] quantities);

    boolean purchaseCart(int userID);

    Shop openShop(int userID, String name, String category);

    void addProductToShop(int userID, int shopID,Product product,int ID,int quantityInStock,double price);

    ProductInShop searchProductInShop(int productID, Shop shop);

    boolean updateProduct(int userID, int shopID, int productID, int newID, int newQuantity, double newPrice);

    boolean deleteProductFromShop(int userID, int shopID, int productID);

    boolean appointOwner(int shopID, int appointerID, int appointeeID);

    boolean appointManager(int shopID, int appointerID, int appointeeID);

    boolean closeShop(int shopID, int userID);

    boolean addPermisionToManager(int id, int id1, int id2, String change_policies);
}
