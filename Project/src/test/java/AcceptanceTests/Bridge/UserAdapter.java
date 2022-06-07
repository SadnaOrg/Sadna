package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;
import ServiceLayer.Objects.Cart;
import ServiceLayer.Objects.ShopsInfo;
import ServiceLayer.Response;
import ServiceLayer.Result;
import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.UserService;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class UserAdapter implements UserBridge{
    protected HashMap<String, UserService> users;
    protected HashMap<String, SubscribedUserService> subscribedUsers;

    public UserAdapter(HashMap<String,UserService> guests, HashMap<String,SubscribedUserService> subscribed){
        this.users = guests;
        this.subscribedUsers = subscribed;
    }
    @Override
    public AcceptanceTests.DataObjects.Guest visit() {
        UserService userService = new UserServiceImp();
        Result visited = userService.loginSystem();
        if(visited.isOk()){
            ServiceLayer.Objects.User u = userService.getUserInfo().getElement();
            String name = u.username;
            users.put(name, userService);
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
            Result registered = userService.registerToSystem(info.username,info.password);
            return registered.isOk();
        }
        return false;
    }

    @Override
    public boolean exit(String username) {
        boolean loggedOut = false;
        if(users.containsKey(username)){
            UserService userService = users.get(username);
            loggedOut = userService.logoutSystem().isOk();
            if(loggedOut)
                users.remove(username);
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
// ADD SUBSCRIBED USER PURCHASE
    @Override
    public boolean purchaseCart(String username,String creditCard, int CVV, int expirationMonth, int expirationYear) {
        UserService service = getService(username);
        if(service == null)
            return false;
        Result purchased = service.purchaseCartFromShop(creditCard,CVV,expirationMonth,expirationYear);
        return purchased.isOk();
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

    public HashMap<String, UserService> getGuests() {
        return users;
    }

    public HashMap<String, SubscribedUserService> getSubscribed() {
        return subscribedUsers;
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
