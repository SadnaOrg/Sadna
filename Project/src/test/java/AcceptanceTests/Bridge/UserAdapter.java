package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;
import ServiceLayer.Objects.Cart;
import ServiceLayer.Response;
import ServiceLayer.Result;
import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.UserService;

import java.util.HashMap;
import java.util.List;

public class UserAdapter implements UserBridge{
    protected HashMap<String, UserService> users = new HashMap<>();
    protected HashMap<String, SubscribedUserService> subscribedUsers= new HashMap<>();
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
            loggedOut = userService.logoutSystem().isOk();
            if(loggedOut)
                subscribedUsers.remove(username);
        }
        return loggedOut;
    }

    @Override
    public List<AcceptanceTests.DataObjects.Shop> getShopsInfo(ShopFilter shopFilter) {
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
    public boolean purchaseCart(String username,String creditCard, int CVV, int expirationMonth, int expirationDay) {
        if(users.containsKey(username)){
            UserService userService = users.get(username);
            Result purchased = userService.purchaseCartFromShop(creditCard,CVV,expirationMonth,expirationDay);
            return purchased.isOk();
        }
        return false;
    }

    @Override
    public ProductInShop searchProductInShop(int productID, int shopID) {
        return null;
    }

    public HashMap<String, UserService> getGuests() {
        return users;
    }

    public HashMap<String, SubscribedUserService> getSubscribed() {
        return subscribedUsers;
    }
}
