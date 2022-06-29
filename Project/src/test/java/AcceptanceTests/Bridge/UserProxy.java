package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.UserService;

import java.util.HashMap;
import java.util.List;

public class UserProxy implements UserBridge{
    protected UserBridge adapter;

    public UserProxy(){
        this.adapter = new UserAdapter(new HashMap<>(), new HashMap<>());
    }

    public UserProxy(SubscribedUserBridge subscribedUserBridge){
        this.adapter = subscribedUserBridge;
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
    public List<Shop> getShopsInfo(String username,ShopFilter shopFilter) {
        return adapter.getShopsInfo(username,shopFilter);
    }

    @Override
    public List<ProductInShop> searchShopProducts(String username,int shopID) {
        return adapter.searchShopProducts(username, shopID);
    }

    @Override
    public List<ProductInShop> searchProducts(String username,ProductFilter productFilter) {
        return adapter.searchProducts(username,productFilter);
    }

    @Override
    public List<ProductInShop> filterShopProducts(String username,int shopID, ProductFilter productFilter) {
        return adapter.filterShopProducts(username,shopID,productFilter);
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
    public double purchaseCart(String username,String creditCard, int CVV, int expirationMonth, int expirationDay) {
        return adapter.purchaseCart(username,creditCard,CVV,expirationMonth,expirationDay);
    }

    @Override
    public ProductInShop searchProductInShop(String username,int productID, int shopID) {
        return adapter.searchProductInShop(username,productID,shopID);
    }

    @Override
    public boolean registerToNotifier(String username) {
        return adapter.registerToNotifier(username);
    }

    @Override
    public List<AcceptanceTests.DataObjects.Notification> getDelayNotification(String username) {
        return adapter.getDelayNotification(username);
    }

    @Override
    public List<Notification> getNotifications(String username) {
        return adapter.getNotifications(username);
    }

    @Override
    public boolean saveProductsAsBid(String username,int shopId, int productId, int quantity, double price) {
        return adapter.saveProductsAsBid(username,shopId,productId,quantity,price);
    }

    protected HashMap<String, UserService> getGuests() {
        return ((UserAdapter)adapter).getGuests();
    }

    protected HashMap<String, SubscribedUserService> getSubscribed() {
        return ((UserAdapter)adapter).getSubscribed();
    }

    protected HashMap<String,List<Notification>> getNotifications(){
        return ((UserAdapter)adapter).getUserNotifications();
    }
}
