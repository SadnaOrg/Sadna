package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;
import ServiceLayer.Response;
import ServiceLayer.Result;
import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.UserService;

import java.util.HashMap;
import java.util.List;
// make a method:get subscribed user in the service
public class UserAdapter implements UserBridge{
    private HashMap<String, UserService> users = new HashMap<>();
    private HashMap<String, SubscribedUserService> subscribedUsers= new HashMap<>();
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
        UserService userService = users.getOrDefault(guestName,null);

        if(userService != null){
            Response<SubscribedUserService> subscribedUserService = userService.login(info.username, info.password);
            if(subscribedUserService.isOk()){
                Response<ServiceLayer.Objects.User> userResponse = subscribedUserService.getElement().getUserInfo();

            }
        }
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
