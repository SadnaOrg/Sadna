package ServiceLayer;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Shops.ShopController;
import BusinessLayer.Shops.ShopFilters;
import BusinessLayer.Shops.ShopInfo;
import BusinessLayer.Users.*;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.SystemService;
import ServiceLayer.interfaces.UserService;
import BusinessLayer.System.System;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class UserServiceImp implements UserService {
    protected User currUser;
    protected UserController userController = UserController.getInstance();
    protected ShopController shopController = ShopController.getInstance();
    protected System system = System.getInstance();

    @Override
    public Result purchaseCartFromShop() {
        return ifUserNotNull(()-> {
            ConcurrentHashMap<Integer, Double> prices = shopController.purchaseBasket(currUser.getName());
            ConcurrentHashMap<Integer, Boolean> paymentSituation = system.pay(prices);
            boolean res = shopController.addToPurchaseHistory(currUser.getName(), paymentSituation);
            if (res)
                Log.getInstance().event("purchased cart succeeded");
            else
                Log.getInstance().event("purchase cart failed");
            return true;
        });
    }

    @Override
    public Result saveProducts(int shopId, int productId, int quantity){
        return ifUserNotNull(()-> {
            boolean res = userController.saveProducts(currUser,shopId,productId,quantity);
            if (res)
                Log.getInstance().event("product save succeeded");
            else
                Log.getInstance().event("product save failed");
            return res;
        });
    }

    @Override
    public Response<Map<Integer, BasketInfo>> showCart() {
        return ifUserNotNullRes(()-> {
            ConcurrentHashMap<Integer, BasketInfo> res = userController.showCart(currUser);
            Log.getInstance().event("cart showed");
            return res;
        });
    }

    @Override
    public Result removeProduct(int shopId, int productId) {
        return ifUserNotNull(()-> {
            boolean res = userController.removeproduct(currUser,shopId,productId);
            if (res) {
                Log.getInstance().event("product removed successfully");
            } else {
                Log.getInstance().event("product removal failed");
            }
            return res;
        });
    }

    @Override
    public Result editProductQuantity(int shopId, int productId, int newQuantity) {
        return ifUserNotNull(()-> {
            boolean res = userController.editProductQuantity(currUser,shopId,productId,newQuantity);
            if (res)
                Log.getInstance().event("edit product quantity succeeded");
            else
                Log.getInstance().event("edit product quantity failed");
            return res;
        });
    }

    @Override
    public Response<ConcurrentHashMap<Integer, ShopInfo>> receiveInformation(){
        return ifUserNotNullRes(()->userController.reciveInformation());
    }

    @Override
    public Result loginSystem() {
        return ifUserNull(()->{
            currUser =userController.loginSystem();
            return currUser!=null;
        });
    }

    @Override
    public Result logoutSystem() {
        return ifUserNotNull(() -> {
            if (userController.logoutSystem(currUser.getUserName()))
                currUser = null;
            return currUser == null;
        });
    }

    @Override
    public Result registerToSystem(String userName, String password){
            return Result.tryMakeResult(()-> {
                boolean res = userController.registerToSystem(userName,password);
                if (res)
                    Log.getInstance().event("register to system succeeded");
                return res;
            },"error in register");
    }

    @Override
    public Response<SubscribedUserService> login(String username, String password){
        return Response.tryMakeResponse(()-> {
            SubscribedUser su = userController.login(username, password,currUser);
            Log.getInstance().event("login succeeded");
            return su;
        } ,"incorrect user name or password").safe(SubscribedUserServiceImp::new);
    }

    @Override
    public Result logout(String username) {
        return Result.tryMakeResult(()-> {
            boolean res = userController.logout(username);
            if (res)
                Log.getInstance().event("logout succeeded");
            return res;
        } ,"incorrect user name or password");
    }

    @Override
    public Response<Map<Shop, Collection<Product>>> searchProducts(ShopFilters shopPred, ProductFilters productPred){
        return ifUserNotNullRes(()->{
            Map<Shop, Collection<Product>> products = shopController.searchProducts(shopPred,productPred);
            Log.getInstance().event("search products succeeded");
            return products;
        });
    }

    private Result ifUserNotNull(Supplier<Boolean> s){
        return Result.tryMakeResult((() -> currUser != null && s.get()) ,"log in to system first");
    }

    private Result ifUserNull(Supplier<Boolean> s){
        return Result.tryMakeResult((() -> currUser == null && s.get()) ,"logout from system first");
    }

    private <T> Response<T> ifUserNotNullRes(Supplier<T> s){
        return Response.tryMakeResponse((() -> currUser == null ? null:  s.get()),"log in to system first");
    }

    private <T> Response<T> ifUserNullRes(Supplier<T> s){
        return Response.tryMakeResponse((() -> currUser != null ? null:  s.get()),"logout from system first");
    }


}
