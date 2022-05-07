package BusinessLayer;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Shops.*;
import BusinessLayer.System.PaymentMethod;
import BusinessLayer.System.System;
import BusinessLayer.Users.*;
import BusinessLayer.Users.BaseActions.BaseActionType;

import javax.naming.NoPermissionException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Facade{
    private final UserController userController = UserController.getInstance();
    private final ShopController shopController= ShopController.getInstance();

    private Facade() {
    }

    public static Facade getInstance(){
        return FacadeHolder.facade;
    }

    public boolean removeproduct(User currUser, int shopId, int productId) {
        return userController.removeproduct(currUser,shopId,productId);
    }

    public ConcurrentHashMap<Integer, ShopInfo> reciveInformation() {
        return userController.reciveInformation();
    }

    public boolean logout(User currUser) {
        return userController.logout(currUser.getUserName());
    }

    public Shop openShop(SubscribedUser currUser,String name) {
        return shopController.openShop(currUser,name);
    }

    public boolean assignShopManager(SubscribedUser currUser, int shop, String userNameToAssign) throws NoPermissionException {
        return  userController.assignShopManager(currUser,shop,userNameToAssign);
      }

    public boolean assignShopOwner(SubscribedUser currUser, int shop, String userNameToAssign) throws NoPermissionException {
        return userController.assignShopOwner(currUser,shop,userNameToAssign);
    }

    public boolean changeManagerPermission(SubscribedUser currUser,int shop, String userNameToAssign, Collection<BaseActionType> types) throws NoPermissionException {
        return userController.changeManagerPermission(currUser,shop,userNameToAssign,types);
    }

    public boolean closeShop(SubscribedUser currUser, int shop) throws NoPermissionException {
        return  userController.closeShop(currUser,shop);
    }

    public Collection<AdministratorInfo> getAdministratorInfo(SubscribedUser currUser, int shop) throws NoPermissionException {
        return  userController.getAdministratorInfo(currUser,shop);
    }

    public Collection<PurchaseHistory> getHistoryInfo(SubscribedUser currUser, int shop) throws NoPermissionException {
        return  userController.getHistoryInfo(currUser,shop);
    }

    public boolean loginSystem(){
        return userController.loginSystem()!=null;
    }

    public boolean logoutSystem(User currUser) {
        return userController.logoutSystem(currUser.getUserName());
    }

    public boolean registerToSystem(String userName, String password) {
        return userController.registerToSystem(userName,password);
    }

    public SubscribedUser login(String username, String password,User currUser) {
        return userController.login(username,password,currUser);
    }

    public boolean logout(String username) {
        return userController.logout(username);
    }

    public Map<Shop, Collection<Product>> searchProducts(ShopFilters shopPred, ProductFilters productPred) {
        return shopController.searchProducts(shopPred,productPred);
    }

    public boolean saveProducts(User currUser, int shopId, int productId, int quantity) {
        return userController.saveProducts(currUser,shopId,productId,quantity);
    }

    public ConcurrentHashMap<Integer, BasketInfo> showCart(User currUser) {
        return userController.showCart(currUser);
    }

    public boolean removeProduct(int shopId, int productId, User currUser) {
        return userController.removeproduct(currUser,shopId,productId);
    }

    public boolean editProductQuantity(User currUser, int shopId, int productId, int newQuantity) {
        return  userController.editProductQuantity(currUser,shopId,productId,newQuantity);
    }

    public boolean purchaseCartFromShop(User currUser, PaymentMethod method) {
        ConcurrentHashMap<Integer, Double> prices = shopController.purchaseBasket(currUser.getName());
        ConcurrentHashMap<Integer, Boolean> paymentSituation = System.getInstance().pay(prices, method);
        return shopController.addToPurchaseHistory(currUser.getName(), paymentSituation);               //todo: make it return payment
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo(SystemManager currUser, int shop, String userName) {
        return  userController.getShopsAndUsersInfo(currUser,shop,userName);
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo(SystemManager currUser,String userName) {
        return userController.getShopsAndUsersInfo(currUser,userName);
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo(SystemManager currUser,int shop) {
        return  userController.getShopsAndUsersInfo(currUser,shop);
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo(SystemManager currUser) {
        return  userController.getShopsAndUsersInfo(currUser);
    }

    private static class FacadeHolder{
        private static Facade facade= new Facade();
    }
}
