package Bridge;

import Mocks.*;

import java.util.List;

public class UserProxy implements UserBridge{

    protected UserBridge real = null;
    protected ShopsBridge shopsBridge = null;

    public void setRealBridge(UserBridge real) {
        this.real = real;
    }

    @Override
    public Guest visit() {
        return null;
    }

    @Override
    public SubscribedUser register(int guestID, RegistrationInfo info) {
        return null;
    }

    @Override
    public boolean exit(int userID) {
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
    public boolean addProductToCart(int userID,int shopID,int productID, int quantity) {
        return false;
    }

    @Override
    public ShoppingCart checkCart(int userID) {
        return null;
    }

    @Override
    public boolean updateCart(int userID,int[] productsIDS, int[] shopsIDS,int[] quantities) {
        return false;
    }

    @Override
    public boolean purchaseCart(int userID) {
        return false;
    }

    @Override
    public ProductInShop searchProductInShop(int productID, int shopID) {
        return null;
    }
}
