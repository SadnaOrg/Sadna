package Bridge;

import Mocks.*;
import Mocks.Guest;
import Mocks.SubscribedUser;
import Mocks.RegistrationInfo;

import java.util.List;
import java.util.Map;

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
    public SubscribedUser login(Guest guest, RegistrationInfo info) {
        return null;
    }

    @Override
    public SubscribedUser register(Guest guest, RegistrationInfo info) {
        return null;
    }

    @Override
    public Guest logout(SubscribedUser user) {
        return null;
    }

    @Override
    public boolean exit(User user) {
        return false;
    }

    @Override
    public List<Shop> getShopsInfo(ShopFilter shopFilter) {
        return null;
    }

    @Override
    public List<Product> searchShopProducts(Shop shop) {
        return null;
    }

    @Override
    public List<Product> searchProducts(ProductFilter productFilter) {
        return null;
    }

    @Override
    public List<ProductInShop> filterShopProducts(int shopID, ProductFilter productFilter) {
        return null;
    }

    @Override
    public ShoppingCart addProductToCart(int userID,int shopID,int productID, int quantity) {
        return null;
    }

    @Override
    public ShoppingCart checkCart(int userID) {
        return null;
    }

    @Override
    public ShoppingCart updateCart(int userID,int[] productsIDS, int[] shopsIDS,int[] quantities) {
        return null;
    }

    @Override
    public boolean purchaseCart(int userID) {
        return false;
    }

    @Override
    public Shop openShop(int userID, String name, String category) {
        return null;
    }

    @Override
    public void addProductToShop(int userID, int shopID, Product product, int ID, int quantityInStock, double price) {

    }

    @Override
    public ProductInShop searchProductInShop(int productID, int shopID) {
        return null;
    }

    @Override
    public boolean updateProduct(int userID, int shopID, int productID, int newID, int newQuantity, double newPrice) {
        return false;
    }

    @Override
    public boolean deleteProductFromShop(int userID, int shopID, int productID) {
        return false;
    }

    @Override
    public boolean appointOwner(int shopID, int appointerID, int appointeeID) {
        return false;
    }

    @Override
    public boolean appointManager(int shopID, int appointerID, int appointeeID) {
        return false;
    }

    @Override
    public boolean closeShop(int shopID, int userID) {
        return false;
    }

    @Override
    public boolean addPermission(int shopiID, int giverID, int receiverID, String permission) {
        return false;
    }

    @Override
    public Map<Integer, Appointment> getShopAppointments(int requestingUserID, int shopID) {
        return null;
    }

    @Override
    public Map<Integer, List<String>> getShopPermissions(int requestingUserID, int shopID) {
        return null;
    }
}
