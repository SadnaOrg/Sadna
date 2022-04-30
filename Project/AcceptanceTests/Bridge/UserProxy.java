package Bridge;

import Mocks.*;
import Tests.UserTests;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Mockito.when;

public class UserProxy implements UserBridge{
    @Mock
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
    public void addProductToCart(int userID,int shopID,int productID, int quantity) {

    }

    @Override
    public ShoppingCart checkCart(int userID) {
        return null;
    }

    @Override
    public void updateCart(int userID,int[] productsIDS, int[] shopsIDS,int[] quantities) {

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
