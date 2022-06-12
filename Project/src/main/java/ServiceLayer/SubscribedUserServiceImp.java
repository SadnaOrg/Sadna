package ServiceLayer;

import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.SystemManager;
import ServiceLayer.Objects.*;
import ServiceLayer.Objects.Policies.Discount.DiscountPred;
import ServiceLayer.Objects.Policies.Discount.DiscountRules;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.SystemManagerService;
import ServiceLayer.interfaces.UserService;

import javax.naming.NoPermissionException;
import java.time.LocalTime;
import java.util.Collection;

public class SubscribedUserServiceImp extends UserServiceImp implements SubscribedUserService {
    SubscribedUser currUser;

    public SubscribedUserServiceImp(SubscribedUser currUser) {
        super(currUser);
        setCurrUser(currUser);
    }


    @Override
    public Response<SystemManagerService> manageSystemAsSystemManager(){
        return ifUserNotNullRes(()-> (currUser instanceof SystemManager) ? ((SystemManager) currUser) : null,"switch to system manager "+currUser.getUserName()).safe(SystemManagerServiceImp::new);
    }

    @Override
    public Response<UserService> logout() {
        if (currUser != null)
            return Response.tryMakeResponse(()-> facade.logout(currUser.getUserName()),"logout " ,"incorrect user name or password").safe(UserServiceImp::new);
        return Response.makeResponse(null, "not logged in");

    }

    @Override
    public Response<SubscribedUserService> login(String username, String password) {
        return Response.tryMakeResponse(()-> facade.login(username, password,currUser),"login - user "+username ,"incorrect user name or password")
                .safe((u)->{this.setCurrUser(u);
                    return this;},"login","");
    }

    @Override
    public Response<Shop> openShop(String name, String desc){
        return ifUserNotNullRes(()->new Shop(facade.openShop(currUser,name, desc)),"open shop succeeded");
    }

    @Override
    public Result assignShopManager(int shop, String userNameToAssign) {
        return ifUserNotNull(()-> facade.assignShopManager(currUser,shop,userNameToAssign),(currUser.getUserName() + "assign "+userNameToAssign+" to shop manager "));
    }

    @Override
    public Result assignShopOwner(int shop, String userNameToAssign){
        return ifUserNotNull(()->  facade.assignShopOwner(currUser,shop,userNameToAssign),(currUser.getUserName() + "assign "+userNameToAssign+" to shop owner "));
    }

    @Override
    public Result changeManagerPermission(int shop, String userNameToAssign, Collection<Integer> types){
        return ifUserNotNull(()->  facade.changeManagerPermission(currUser,shop,userNameToAssign,types),"change manager permission succeeded");
    }

    @Override
    public Result closeShop(int shop){
        return ifUserNotNull(()-> facade.closeShop(currUser,shop),"close shop succeeded");
    }

    @Override
    public Response<AdministratorInfo> getAdministratorInfo(int shop){
        return ifUserNotNullRes(()-> new AdministratorInfo(facade.getAdministratorInfo(currUser,shop)), currUser.getUserName()+"get administrator info ");
    }

    @Override
    public Response<PurchaseHistoryInfo> getHistoryInfo(int shop){
        return ifUserNotNullRes(()->  new PurchaseHistoryInfo(facade.getHistoryInfo(currUser,shop)), currUser.getUserName()+" get history info");
    }

    @Override
    public Result updateProductQuantity(int shopID, int productID, int newQuantity){
        return ifUserNotNullStockManagement(() -> facade.updateProductQuantity(currUser.getUserName(),shopID,productID,newQuantity), "update product quantity");
    }

    @Override
    public Result updateProductPrice(int shopID, int productID, double newPrice) {
        return ifUserNotNullStockManagement(() -> facade.updateProductPrice(currUser.getUserName(),shopID,productID,newPrice), "update product price");
    }

    @Override
    public Result updateProductDescription(int shopID, int productID, String Desc) {
        return ifUserNotNullStockManagement(() -> facade.updateProductDescription(currUser.getUserName(),shopID,productID,Desc), "update product description");
    }

    @Override
    public Result updateProductName(int shopID, int productID, String newName) {
        return ifUserNotNullStockManagement(() -> facade.updateProductName(currUser.getUserName(),shopID,productID,newName), "update product name");
    }


    @Override
    public Result deleteProductFromShop(int shopID, int productID) {
        return ifUserNotNullStockManagement(() -> facade.deleteProductFromShop(currUser.getUserName(), shopID,productID), "remove product from shop");
    }

    @Override
    public Result addProductToShop(int shopID, String name,String desc, String manufacturer, int productID, int quantity, double price){
        return ifUserNotNullStockManagement(() -> facade.addProductToShop(currUser.getUserName(), shopID,name,manufacturer,desc,productID,quantity,price),
                "adding product to shop");
    }

    @Override
    public Result reopenShop(int shopID) {
        return ifUserNotNull(() -> facade.reopenShop(currUser.getUserName(),shopID),"reopen shop", "you aren't a founder!");
    }

    public Response<ServiceLayer.Objects.SubscribedUser> getSubscribedUserInfo(){
        return ifUserNotNullRes(() -> new ServiceLayer.Objects.SubscribedUser(currUser), "getting user info");
    }

    @Override
    public Response<Administrator> getMyInfo(int shopID) {
        return ifUserNotNullRes(() -> new Administrator(facade.getMyInfo(currUser.getUserName(),shopID)),"permission check");
    }

    @Override
    public Result removeAdmin(int shopID, String toRemove) {
        return ifUserNotNull(() -> facade.removeAdmin(shopID, currUser.getUserName(), toRemove),"removing admin appointment");
    }
    @Override
    public Response<Integer> createProductByQuantityDiscount(int productId, int productQuantity, double discount, int connectId, int shopId)  {
        return ifUserNotNullRes(()-> facade.createProductByQuantityDiscount(currUser,productId, productQuantity, discount, connectId, shopId) ,"add discount succeeded");
    }

    @Override
    public Response<Integer> createProductDiscount(int productId, double discount, int connectId, int shopId)  {
        return ifUserNotNullRes(()-> facade.createProductDiscount(currUser,productId, discount, connectId, shopId),"add discount succeeded");
    }

    @Override
    public Response<Integer> createProductQuantityInPriceDiscount(int productID, int quantity, double priceForQuantity, int connectId, int shopId)  {
        return ifUserNotNullRes(()-> facade.createProductQuantityInPriceDiscount(currUser,productID, quantity, priceForQuantity, connectId, shopId),"add discount succeeded");
    }

    @Override
    public Response<Integer> createRelatedGroupDiscount(Collection<Integer> relatedProducts, double discount, int connectId , int shopId)  {
        return ifUserNotNullRes(()-> facade.createRelatedGroupDiscount(currUser,relatedProducts, discount, connectId, shopId),"add discount succeeded");
    }

    @Override
    public Response<Integer> createShopDiscount(int basketQuantity,double discount,int connectId, int shopId)  {

        return ifUserNotNullRes(()-> facade.createShopDiscount(currUser,basketQuantity, discount, connectId, shopId),"add discount succeeded");
    }

    @Override
    public Response<Integer> createDiscountAndPolicy(ServiceLayer.Objects.Policies.Discount.DiscountPred discountPred, ServiceLayer.Objects.Policies.Discount.DiscountRules discountPolicy, int connectId, int shopId)  {
        return ifUserNotNullRes(()-> facade.createDiscountAndPolicy(currUser,
                ServiceLayer.Objects.Policies.Discount.DiscountPred.makeBusinessPred(discountPred),
                ServiceLayer.Objects.Policies.Discount.DiscountRules.makeBusinessRule(discountPolicy),
                connectId, shopId),"add discount succeeded");
    }

    @Override
    public Response<Integer> createDiscountMaxPolicy(ServiceLayer.Objects.Policies.Discount.DiscountRules discountPolicy,int connectId, int shopId)  {
        return ifUserNotNullRes(()-> facade.createDiscountMaxPolicy(currUser,
                ServiceLayer.Objects.Policies.Discount.DiscountRules.makeBusinessRule(discountPolicy)
                , connectId, shopId),"add discount succeeded");
    }

    @Override
    public Response<Integer>  createDiscountOrPolicy(ServiceLayer.Objects.Policies.Discount.DiscountPred discountPred, ServiceLayer.Objects.Policies.Discount.DiscountRules discountPolicy,int connectId, int shopId)  {
        return ifUserNotNullRes(()-> facade.createDiscountOrPolicy(currUser,
                ServiceLayer.Objects.Policies.Discount.DiscountPred.makeBusinessPred(discountPred),
                ServiceLayer.Objects.Policies.Discount.DiscountRules.makeBusinessRule(discountPolicy),
                connectId, shopId),"add discount succeeded");
    }

    @Override
    public Response<Integer>  createDiscountPlusPolicy(ServiceLayer.Objects.Policies.Discount.DiscountRules discountPolicy,int connectId, int shopId)  {
        return ifUserNotNullRes(()-> facade.createDiscountPlusPolicy(currUser,
                ServiceLayer.Objects.Policies.Discount.DiscountRules.makeBusinessRule(discountPolicy)
                , connectId, shopId),"add discount succeeded");
    }

    @Override
    public Response<Integer> createDiscountXorPolicy(ServiceLayer.Objects.Policies.Discount.DiscountRules  discountRules1, ServiceLayer.Objects.Policies.Discount.DiscountRules  discountRules2, DiscountPred tieBreaker, int connectId, int shopId)  {
        return ifUserNotNullRes(()-> facade.createDiscountXorPolicy(currUser,
                ServiceLayer.Objects.Policies.Discount.DiscountRules.makeBusinessRule(discountRules1),
                ServiceLayer.Objects.Policies.Discount.DiscountRules.makeBusinessRule(discountRules2),
                ServiceLayer.Objects.Policies.Discount.DiscountPred.makeBusinessPred(tieBreaker)
                , connectId, shopId),"add discount succeeded");
    }

    @Override
    public Response<Integer>   createValidateBasketQuantityDiscount(int basketquantity, boolean cantBeMore ,int connectId, int shopId)  {
        return ifUserNotNullRes(()-> facade.createValidateBasketQuantityDiscount(currUser,basketquantity, cantBeMore, connectId, shopId),"add discount predicate succeeded");
    }

    @Override
    public Response<Integer>  createValidateBasketValueDiscount(double basketvalue ,boolean cantBeMore,int connectId, int shopId)  {
        return ifUserNotNullRes(()-> facade.createValidateBasketValueDiscount(currUser,basketvalue, cantBeMore, connectId, shopId),"add discount predicate succeeded");
    }

    @Override
    public Response<Integer>  createValidateProductQuantityDiscount(int productId, int productQuantity, boolean cantbemore ,int connectId, int shopId)  {

        return ifUserNotNullRes(()-> facade.createValidateProductQuantityDiscount(currUser,productId, productQuantity, cantbemore, connectId, shopId),"add discount predicate succeeded");
    }

    @Override
    public Response<Integer> createValidateProductPurchase(int productId, int productQuantity, boolean cantbemore, int connectId, int shopId){
        return ifUserNotNullRes(()-> facade.createValidateProductPurchase(currUser, productId, productQuantity, cantbemore, connectId, shopId),"add purchase policy succeeded");

    }

    @Override
    public Response<Integer> createValidateTImeStampPurchase(LocalTime localTime, boolean buybefore, int conncectId, int shopId){
        return ifUserNotNullRes(()-> facade.createValidateTImeStampPurchase(currUser, localTime,buybefore,conncectId,shopId),"add purchase policy succeeded");
    }
    public Response<Boolean> removeDiscount(SubscribedUser currUser, DiscountRules discountRules, int shopId) throws NoPermissionException {
        return ifUserNotNullRes(()-> facade.removeDiscount(currUser,
                DiscountRules.makeBusinessRule(discountRules),
                shopId),"remove purchase policy succeeded");
    }

    public Response<Boolean> removePredicate(SubscribedUser currUser, DiscountPred discountPred, int shopId) throws NoPermissionException {
        return ifUserNotNullRes(()-> facade.removePredicate(currUser,
                DiscountPred.makeBusinessPred(discountPred),
                shopId),"remove purchase policy succeeded");
    }

    protected void setCurrUser(SubscribedUser currUser) {
        this.currUser = currUser;
        super.currUser = currUser;
    }

    private Result ifUserNotNull(MySupplier<Boolean> s,String eventName, String errMsg){
        return Result.tryMakeResult((() -> currUser != null && currUser.isLoggedIn()&& s.get()) ,eventName,errMsg);
    }

    private Result ifUserNotNull(MySupplier<Boolean> s,String eventName){
        return Result.tryMakeResult((() -> currUser != null && currUser.isLoggedIn()&& s.get()) ,eventName,"log in to the system first as a subscribed user");
    }

    private Result ifUserNotNullStockManagement(MySupplier<Boolean> s,String eventName){
        String msg = "Failed to " + eventName;
        return Result.tryMakeResult((() -> currUser != null && currUser.isLoggedIn()&& s.get()) ,eventName,msg);
    }

    private Result ifUserNull(MySupplier<Boolean> s,String eventName){
        return Result.tryMakeResult((() -> currUser == null && s.get()) ,eventName,"logout from system first");
    }

    private <T> Response<T> ifUserNotNullRes(MySupplier<T> s,String eventName){
        return Response.tryMakeResponse((() -> currUser == null|| !currUser.isLoggedIn() ? null:  s.get()),eventName,"log in to the system first as a subscribed user");
    }

    private <T> Response<T> ifUserNullRes(MySupplier<T> s,String eventName){
        return Response.tryMakeResponse((() -> currUser != null ? null:  s.get()),eventName ,"logout from system first");
    }


}
