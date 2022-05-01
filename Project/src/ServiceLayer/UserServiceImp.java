package ServiceLayer;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Shops.ShopController;
import BusinessLayer.Shops.ShopFilters;
import BusinessLayer.Shops.ShopInfo;
import BusinessLayer.Users.*;
import ServiceLayer.interfaces.SubscribedUserService;
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
            shopController.addToPurchaseHistory(currUser.getName(), paymentSituation);
            return true;
        });
    }

    @Override
    public Result saveProducts(int shopId, int productId, int quantity){
        return ifUserNotNull(()-> userController.saveProducts(currUser,shopId,productId,quantity));
    }

    @Override
    public Response<Map<Integer, BasketInfo>> showCart(){
        return ifUserNotNullRes(()->userController.showCart(currUser));
    }

    @Override
    public Result removeProduct(int shopId, int productId) {
        return ifUserNotNull(()->userController.removeproduct(currUser,shopId,productId));
    }

    @Override
    public Result editProductQuantity(int shopId, int productId, int newQuantity) {
        return ifUserNotNull(()->userController.editProductQuantity(currUser,shopId,productId,newQuantity));
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
            return Result.tryMakeResult(()->userController.registerToSystem(userName,password),"error in register");
    }

    @Override
    public Response<SubscribedUserService> login(String username, String password){
        return Response.tryMakeResponse(()-> userController.login(username, password,currUser) ,"incorrect user name or password").safe(SubscribedUserServiceImp::new);
    }

    @Override
    public Result logout(String username){
        return Result.tryMakeResult(()-> userController.logout(username) ,"incorrect user name or password");
    }

    @Override
    public Response<Map<Shop, Collection<Product>>> searchProducts(ShopFilters shopPred, ProductFilters productPred){
        return ifUserNotNullRes(()->shopController.searchProducts(shopPred,productPred));
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
