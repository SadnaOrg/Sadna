package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;
import ServiceLayer.Objects.Cart;
import ServiceLayer.Objects.Notification;
import ServiceLayer.Objects.ShopsInfo;
import ServiceLayer.Response;
import ServiceLayer.Result;
import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.UserService;

import java.util.*;
import java.util.function.Function;

public class UserAdapter implements UserBridge{
    protected HashMap<String, UserService> users;
    protected HashMap<String, SubscribedUserService> subscribedUsers;
    protected HashMap<String,List<AcceptanceTests.DataObjects.Notification>> userNotifications;

    public UserAdapter(HashMap<String,UserService> guests, HashMap<String,SubscribedUserService> subscribed){
        this.users = guests;
        this.subscribedUsers = subscribed;
        this.userNotifications = new HashMap<>();
    }

    public UserAdapter(HashMap<String, UserService> guests, HashMap<String, SubscribedUserService> subscribed, HashMap<String, List<AcceptanceTests.DataObjects.Notification>> notifications) {
        this.users = guests;
        this.subscribedUsers = subscribed;
        this.userNotifications = notifications;
    }

    @Override
    public AcceptanceTests.DataObjects.Guest visit() {
        UserService userService = new UserServiceImp();
        Result visited = userService.loginSystem();
        if(visited.isOk()){
            ServiceLayer.Objects.User u = userService.getUserInfo().getElement();
            String name = u.username;
            users.put(name, userService);
            userNotifications.put(name,new LinkedList<>());
            return new Guest(name);
        }
        return null;
    }

    @Override
    public AcceptanceTests.DataObjects.SubscribedUser login(String guestName, RegistrationInfo info) {
        String username = info.username;
        String password = info.password;
        UserService userService = users.getOrDefault(guestName,null);
        if(userService != null){
            Response<SubscribedUserService> subscribedUserService = userService.login(username, password);
            if(subscribedUserService.isOk()){
                Response<ServiceLayer.Objects.SubscribedUser> userResponse = subscribedUserService.getElement().getSubscribedUserInfo();
                users.remove(guestName);
                subscribedUsers.put(username,subscribedUserService.getElement());
                return new SubscribedUser(userResponse.getElement());
            }
            else
                return null;
        }
        return null;
    }

    @Override
    public boolean register(String guestname,RegistrationInfo info) {
        if(users.containsKey(guestname)){
            UserService userService = users.get(guestname);
            Result registered = userService.registerToSystem(info.username,info.password,new Date(2001, Calendar.DECEMBER,1));
            if(registered.isOk()){
                userNotifications.put(info.username,new LinkedList<>());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean exit(String username) {
        boolean loggedOut = false;
        if(users.containsKey(username)){
            UserService userService = users.get(username);
            loggedOut = userService.logoutSystem().isOk();
            if(loggedOut){
                users.remove(username);
                userNotifications.remove(username);
            }
        }
        else if(subscribedUsers.containsKey(username)){
            SubscribedUserService userService = subscribedUsers.get(username);
            Response<UserService> serviceResponse = userService.logout();
            if(serviceResponse.isOk()){
                UserService service = serviceResponse.getElement();
                subscribedUsers.remove(username);
                return service.logoutSystem().isOk();
            }
        }
        return loggedOut;
    }

    @Override
    public List<AcceptanceTests.DataObjects.Shop> getShopsInfo(String username,ShopFilter shopFilter) {
        UserService service = getService(username);
        if(service == null)
            return null;
        Response<ShopsInfo> infoResponse = service.searchProducts((shop) -> shopFilter.filter(new Shop(shop)), (product -> true));
        if(infoResponse.isOk()){
            ShopsInfo info = infoResponse.getElement();
            if(info.shops().size() == 0)
                return null;
            List<Shop> shops = new LinkedList<>();
            for (ServiceLayer.Objects.Shop shopInfo:
                    info.shops()) {
                shops.add(new Shop(shopInfo));
            }
            return shops;
        }
        return null;
    }

    @Override
    public List<ProductInShop> searchShopProducts(String username,int shopID) {
        UserService service = getService(username);
        if(service == null){
            return null;
        }
        Response<ShopsInfo> infoResponse = service.searchProducts(shop -> shop.shopId() == shopID,product -> true);
        if(infoResponse.isOk()){
            return getProducts(infoResponse);
        }
        return null;
    }

    @Override
    public List<ProductInShop> searchProducts(String username,ProductFilter productFilter) {
        UserService service = getService(username);
        if(service == null)
            return null;
        Response<ShopsInfo> infoResponse = service.searchProducts(shop -> true,product -> productFilter.filter(new ProductInShop(product)));
        if(infoResponse.isOk()){
            List<ProductInShop> products = getProducts(infoResponse);
            if(products == null)
                return null;
            if(products.size() == 0)
                return null;
            return products;
        }
        else return null;
    }

    @Override
    public List<ProductInShop> filterShopProducts(String username,int shopID, ProductFilter productFilter) {
        UserService service = getService(username);
        if(service == null)
            return null;
        Response<ShopsInfo> infoResponse = service.searchProducts(shop -> shop.shopId() == shopID, product -> productFilter.filter(new ProductInShop(product)));
        if(infoResponse.isOk()){
            return getProducts(infoResponse);
        }
        else return null;
    }

    @Override
    public boolean addProductToCart(String username, int shopID, int productID, int quantity) {
        Result added;
        if(users.containsKey(username)){
            UserService userService = users.get(username);
            added = userService.saveProducts(shopID,productID,quantity);
            return added.isOk();
        }
        else if(subscribedUsers.containsKey(username)){
            SubscribedUserService userService = subscribedUsers.get(username);
            added = userService.saveProducts(shopID,productID,quantity);
            return added.isOk();
        }
        return false;
    }

    @Override
    public ShoppingCart checkCart(String username) {
        ShoppingCart cart=null;
        if(users.containsKey(username)){
            UserService userService = users.get(username);
            Response<Cart> userCart = userService.showCart();
            if(userCart.isOk())
                cart = new ShoppingCart(userCart.getElement());
        }
        else if(subscribedUsers.containsKey(username)){
            SubscribedUserService userService = subscribedUsers.get(username);
            Response<Cart> userCart = userService.showCart();
            if(userCart.isOk())
                cart = new ShoppingCart(userCart.getElement());
        }
        return cart;
    }

    @Override
    public boolean updateCart(String username, int productID, int shopID, int quantity) {
        Result edited;
        if(users.containsKey(username)){
            UserService userService = users.get(username);
            edited = userService.editProductQuantity(shopID,productID,quantity);
            return edited.isOk();
        }
        else if(subscribedUsers.containsKey(username)){
            SubscribedUserService userService = subscribedUsers.get(username);
            edited = userService.editProductQuantity(shopID,productID,quantity);
            return edited.isOk();
        }
        return false;
    }

    @Override
    public double purchaseCart(String username,String creditCard, int CVV, int expirationMonth, int expirationYear) {
        UserService service = getService(username);
        if(service == null)
            return 0;
        Response<Double> purchased = service.purchaseCartFromShop(creditCard,CVV,expirationMonth,expirationYear,"206000556","maor biton");
        if(purchased.isOk())
            return purchased.getElement();
        return 0;
    }

    @Override
    public ProductInShop searchProductInShop(String username,int productID, int shopID) {
        UserService service = getService(username);
        if(service == null)
            return null;
        Response<ShopsInfo> infoResponse = service.searchProducts(shop -> shop.shopId() == shopID, product -> product.productID() == productID);
        if(infoResponse.isOk()){
            List<ProductInShop> productInShops = getProducts(infoResponse);
            if(productInShops == null)
                return null;
            if(productInShops.size() != 1)
                return null;
            else return productInShops.get(0);
        }
        else return null;
    }

    @Override
    public boolean registerToNotifier(String username) {
        UserService service = getService(username);
        if(service != null){
            Function<Notification,Boolean> con = notification -> {
                userNotifications.get(username).add(0, new AcceptanceTests.DataObjects.Notification(notification.Content()));
                return true;
            };

            Result registered = service.registerToNotifier(con);
            return registered.isOk();
        }
        return false;
    }

    @Override
    public List<AcceptanceTests.DataObjects.Notification> getDelayNotification(String username) {
        UserService service = getService(username);
        if(service!=null){
            Result got = service.getDelayNotification();
            if(got.isOk()){
                List<AcceptanceTests.DataObjects.Notification> notifications = userNotifications.getOrDefault(username,null);
                userNotifications.put(username,new LinkedList<>());
                return notifications;
            }
        }
        return null;
    }

    @Override
    public List<AcceptanceTests.DataObjects.Notification> getNotifications(String username) {
        UserService service = getService(username);
        if(service!=null){
           // userNotifications.put(username,new LinkedList<>());
            service.registerToNotifier(not-> userNotifications.get(username).add(new AcceptanceTests.DataObjects.Notification(not.Content())));
            Result response = service.getDelayNotification();
            if(response.isOk()){
                List<AcceptanceTests.DataObjects.Notification> notifications = userNotifications.getOrDefault(username,null);
                userNotifications.put(username,new LinkedList<>());
                return notifications;
            }
        }
        return null;
    }

    @Override
    public boolean saveProductsAsBid(String username,int shopId, int productId, int quantity, double price) {
        UserService service = getService(username);
        if(service != null){
            Result saved = service.saveProductsAsBid(shopId,productId,quantity,price);
            return saved.isOk();
        }
        return false;
    }

    public HashMap<String, UserService> getGuests() {
        return users;
    }

    public HashMap<String, SubscribedUserService> getSubscribed() {
        return subscribedUsers;
    }

    public HashMap<String,List<AcceptanceTests.DataObjects.Notification>> getUserNotifications(){
        return this.userNotifications;
    }

    private UserService getService(String username){
        UserService service;
        if(users.containsKey(username)) {
            service = users.get(username);
        }
        else if(subscribedUsers.containsKey(username)){
            service = subscribedUsers.get(username);
        }
        else {
            return null;
        }
        return service;
    }

    private List<ProductInShop> getProducts(Response<ShopsInfo> infoResponse){
        List<ProductInShop> productInShops = new LinkedList<>();
        ShopsInfo info = infoResponse.getElement();
        Collection<ServiceLayer.Objects.Shop> shops = info.shops();
        if(shops.size() == 0)
            return null;
        for (ServiceLayer.Objects.Shop s:
                info.shops()) {
            Collection<ServiceLayer.Objects.Product> products = s.shopProducts();
            List<ProductInShop> shopsProducts = products.stream().map(ProductInShop::new).toList();
            productInShops.addAll(shopsProducts);
        }
        return productInShops;
    }
}
