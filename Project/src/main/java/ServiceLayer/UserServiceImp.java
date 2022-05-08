package ServiceLayer;

import BusinessLayer.Facade;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Shops.ShopFilters;
import BusinessLayer.System.PaymentMethod;
import BusinessLayer.Users.*;
import ServiceLayer.Objects.Cart;
import ServiceLayer.Objects.ShopsInfo;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.UserService;

import java.util.function.Supplier;

public class UserServiceImp implements UserService {
    protected User currUser;
    protected Facade facade = Facade.getInstance();

    @Override
    public Result purchaseCartFromShop(String creditCardNumber, int CVV, int expiryMonth, int expiryYear) {
        return ifUserNotNull(()-> facade.purchaseCartFromShop(currUser,new PaymentMethod(creditCardNumber,CVV,expiryMonth,expiryYear)),"purchased cart");
    }

    @Override
    public Result saveProducts(int shopId, int productId, int quantity){
        return ifUserNotNull(()-> facade.saveProducts(currUser,shopId,productId,quantity),"product save");
    }

    @Override
    public Response<Cart> showCart() {
        return ifUserNotNullRes(()->new Cart(facade.showCart(currUser), currUser.getUserName()),"cart showed");

    }

    @Override
    public Result removeProduct(int shopId, int productId) {
        return ifUserNotNull(()-> facade.removeproduct(currUser,shopId,productId),"product removed ");
    }

    @Override
    public Result editProductQuantity(int shopId, int productId, int newQuantity) {
        return ifUserNotNull(()-> facade.editProductQuantity(currUser,shopId,productId,newQuantity) ,"edit product quantity ");
    }

    @Override
    public Response<ShopsInfo> receiveInformation(){
        return ifUserNotNullRes(()->new ShopsInfo(facade.reciveInformation()),"receive information");
    }

    @Override
    public Result loginSystem() {
        return ifUserNull(()->facade.loginSystem(),"login to system");
    }

    @Override
    public Result logoutSystem() {
        return ifUserNotNull(() -> facade.logoutSystem(currUser),"logout from system");
    }

    @Override
    public Result registerToSystem(String userName, String password){
            return Result.tryMakeResult(()-> facade.registerToSystem(userName,password),"register to system succeeded","error in register");
    }

    @Override
    public Response<SubscribedUserService> login(String username, String password){
        return Response.tryMakeResponse(()-> facade.login(username, password,currUser),"login " ,"incorrect user name or password").safe(SubscribedUserServiceImp::new);
    }

    @Override
    public Result logout(String username) {
        return Result.tryMakeResult(()-> facade.logout(username),"logout " ,"incorrect user name or password");
    }

    @Override
    public Response<ShopsInfo> searchProducts(ShopFilters shopPred, ProductFilters productPred){
        return ifUserNotNullRes(()->new ShopsInfo(facade.searchProducts(shopPred,productPred)),"search products succeeded");
    }


    private Result ifUserNotNull(Supplier<Boolean> s, String eventName){
        return Result.tryMakeResult((() -> currUser != null && s.get()) ,eventName,"log in to system first");
    }

    private Result ifUserNull(Supplier<Boolean> s, String eventName){
        return Result.tryMakeResult((() -> currUser == null && s.get()) , eventName,"logout from system first");
    }

    private <T> Response<T> ifUserNotNullRes(Supplier<T> s, String eventName){
        return Response.tryMakeResponse((() -> currUser == null ? null:  s.get()), eventName,"log in to system first");
    }

    private <T> Response<T> ifUserNullRes(Supplier<T> s, String eventName){
        return Response.tryMakeResponse((() -> currUser != null ? null:  s.get()),  eventName,"logout from system first");
    }


}
