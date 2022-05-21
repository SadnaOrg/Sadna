package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.UserService;

import java.util.HashMap;
import java.util.List;

public class UserProxy implements UserBridge{
    UserBridge adapter;

    public UserProxy(){
        this.adapter = new UserAdapter();
    }
    @Override
    public Guest visit() {
        return adapter.visit();
    }

    @Override
    public SubscribedUser login(String guestName,RegistrationInfo info) {
        return adapter.login(guestName,info);
    }

    @Override
    public boolean register(String guestname,RegistrationInfo info) {
        return adapter.register(guestname,info);
    }

    @Override
    public boolean exit(String username) {
        return adapter.exit(username);
    }

    @Override
    public List<Shop> getShopsInfo(ShopFilter shopFilter) {
        return adapter.getShopsInfo(shopFilter);
    }

    @Override
    public List<ProductInShop> searchShopProducts(int shopID) {
        return adapter.searchShopProducts(shopID);
    }

    @Override
    public List<ProductInShop> searchProducts(ProductFilter productFilter) {
        return adapter.searchProducts(productFilter);
    }

    @Override
    public List<ProductInShop> filterShopProducts(int shopID, ProductFilter productFilter) {
        return adapter.filterShopProducts(shopID,productFilter);
    }

    @Override
    public boolean addProductToCart(String username, int shopID, int productID, int quantity) {
        return adapter.addProductToCart(username,shopID,productID,quantity);
    }

    @Override
    public ShoppingCart checkCart(String username) {
        return adapter.checkCart(username);
    }

    @Override
    public boolean updateCart(String username, int productID, int shopsID, int quantity) {
        return adapter.updateCart(username,productID,shopsID,quantity);
    }

    @Override
    public boolean purchaseCart(String username,String creditCard, int CVV, int expirationMonth, int expirationDay) {
        return adapter.purchaseCart(username,creditCard,CVV,expirationMonth,expirationDay);
    }

    @Override
    public ProductInShop searchProductInShop(int productID, int shopID) {
        return adapter.searchProductInShop(productID,shopID);
    }

    protected HashMap<String, UserService> getGuests() {
        return ((UserAdapter)adapter).getGuests();
    }

    protected HashMap<String, SubscribedUserService> getSubscribed() {
        return ((UserAdapter)adapter).getSubscribed();
    }
}
