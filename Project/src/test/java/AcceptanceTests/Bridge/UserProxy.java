package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;

import java.util.List;

public class UserProxy implements UserBridge{
    @Override
    public Guest visit() {
        return null;
    }

    @Override
    public SubscribedUser login(String guestName,RegistrationInfo info) {
        return null;
    }

    @Override
    public boolean register(RegistrationInfo info) {
        return false;
    }

    @Override
    public boolean exit(String username) {
        return false;
    }

    @Override
    public List<Shop> getShopsInfo(ShopFilter shopFilter) {
        return null;
    }

    @Override
    public List<ProductInShop> searchShopProducts(int shopID) {
        return null;
    }

    @Override
    public List<ProductInShop> searchProducts(ProductFilter productFilter) {
        return null;
    }

    @Override
    public List<ProductInShop> filterShopProducts(int shopID, ProductFilter productFilter) {
        return null;
    }

    @Override
    public boolean addProductToCart(String username, int shopID, int productID, int quantity) {
        return false;
    }

    @Override
    public ShoppingCart checkCart(String username) {
        return null;
    }

    @Override
    public boolean updateCart(String username, int[] productsIDS, int[] shopsIDS, int[] quantities) {
        return false;
    }

    @Override
    public boolean purchaseCart(String username) {
        return false;
    }

    @Override
    public ProductInShop searchProductInShop(int productID, int shopID) {
        return null;
    }
}
