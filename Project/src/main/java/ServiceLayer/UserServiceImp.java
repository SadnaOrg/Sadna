package ServiceLayer;

import BusinessLayer.Facade;
import BusinessLayer.Mappers.MapperController;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Users.User;
import BusinessLayer.System.PaymentMethod;
import ServiceLayer.Objects.*;;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.UserService;

import java.util.Date;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class UserServiceImp implements UserService {
    protected User currUser;
    protected Facade facade = Facade.getInstance();

    protected UserServiceImp(BusinessLayer.Users.User logged){
        currUser = logged;
    }

    public UserServiceImp(){
        currUser = null;
        MapperController.getInstance();
    }

    @Override
    public Response<Double> purchaseCartFromShop(String creditCardNumber, int CVV, int expiryMonth, int expiryYear) {
        return ifUserNotNullRes(()-> facade.purchaseCartFromShop(currUser,new PaymentMethod(creditCardNumber,CVV,expiryMonth,expiryYear)),"purchased cart");
    }

    @Override
    public Response<Double> getCartPrice() {
        return ifUserNotNullRes(()-> facade.getCartPrice(currUser),"here is the cart value");
    }

    @Override
    public Response<Double> getBasketPrice(int shopid) {
        return ifUserNotNullRes(()-> facade.getBasketPrice(currUser,shopid),"here is the basket value");
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
        return ifUserNotNull(()-> facade.removeProduct(shopId,productId,currUser),"product removed ");
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
        return ifUserNull(()->setUser(),"login to system");
    }

    @Override
    public Result logoutSystem() {
        return ifUserNotNull(() -> facade.logoutSystem(currUser),"logout from system");
    }

    @Override
    public Result registerToSystem(String userName, String password, Date date){
            if (currUser != null)
                return Response.tryMakeResult(()-> facade.registerToSystem(userName,password,date),"register to system succeeded","error in register");
            return Result.makeResult(false, "have to use the system to register!");
    }

    @Override
    public Response<SubscribedUserService> login(String username, String password){
        return Response.tryMakeResponse(()-> facade.login(username, password,currUser),"login " ,"incorrect user name or password").safe(SubscribedUserServiceImp::new);
    }

    @Override
    public Response<ShopsInfo> searchProducts(Predicate<Shop> shopPred, Predicate<Product> productPred){
        return ifUserNotNullRes(()->new ShopsInfo(facade.searchProducts((s)-> s.isOpen() && shopPred.test(new Shop(s)),(p)->productPred.test(new Product(p)))),"search products succeeded");
    }

    @Override
    public Response<ServiceLayer.Objects.User> getUserInfo(){
        return ifUserNotNullRes(()-> new ServiceLayer.Objects.User(currUser),"user details lookup");
    }

    @Override
    public Result registerToNotifier(Function<Notification, Boolean> con){
       return ifUserNotNull(()->facade.registerToNotifier(currUser.getUserName(),(not) -> con.apply(new Notification(not))),"register to notification service");
    }

    @Override
    public Result getDelayNotification(){
        return ifUserNotNull(() -> facade.getDelayedNotifications(currUser),"get all delay notification");
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

    private boolean setUser(){
        currUser = facade.loginSystem();
        return true;
    }

}
