package BusinessLayer;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Users.*;
import BusinessLayer.Shops.*;
import BusinessLayer.System.PaymentMethod;
import BusinessLayer.System.System;

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

    public Guest logout(User currUser) {
        return userController.logout(currUser.getUserName());
    }

    public Shop openShop(SubscribedUser currUser,String name, String desc) {
        return shopController.openShop(currUser,name,desc);
    }

    public boolean assignShopManager(SubscribedUser currUser, int shop, String userNameToAssign) throws NoPermissionException {
        return  userController.assignShopManager(currUser,shop,userNameToAssign);
      }

    public boolean assignShopOwner(SubscribedUser currUser, int shop, String userNameToAssign) throws NoPermissionException {
        return userController.assignShopOwner(currUser,shop,userNameToAssign);
    }

    public boolean changeManagerPermission(SubscribedUser currUser,int shop, String userNameToAssign, Collection<Integer> types) throws NoPermissionException {
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

    public Guest loginSystem(){
        return userController.loginSystem();
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

    public Guest logout(String username) {
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

    public boolean updateProductQuantity(String username,int shopID, int productID, int newQuantity) throws NoPermissionException {
        return userController.updateProductQuantity(username,shopID,productID,newQuantity);
    }

    public boolean updateProductPrice(String username,int shopID, int productID, double newPrice) throws NoPermissionException {
        return userController.updateProductPrice(username,shopID,productID,newPrice);
    }

    public boolean updateProductDescription(String username,int shopID, int productID, String Desc) throws NoPermissionException {
        return userController.updateProductDescription(username,shopID,productID,Desc);
    }

    public boolean updateProductName(String username,int shopID, int productID, String newName) throws NoPermissionException {
        return userController.updateProductName(username,shopID,productID,newName);
    }

    public boolean deleteProductFromShop(String username,int shopID, int productID) throws NoPermissionException {
        return userController.deleteProductFromShop(username,shopID,productID);
    }

    public boolean addProductToShop(String username,int shopID, String name,String manufacturer, String desc, int productID, int quantity, double price) throws NoPermissionException {
        return userController.addProductToShop(username,shopID,name,manufacturer,desc,productID,quantity,price);
    }

    public boolean reopenShop(String userName, int shopID) throws NoPermissionException {
        return userController.reopenShop(userName,shopID);
    }

    public AdministratorInfo getMyInfo(String userName, int shopID) {
        return userController.getMyInfo(userName,shopID);
    }

    public Boolean removeAdmin(int shopID, String requesting, String toRemove) throws NoPermissionException {
        return userController.removeAdmin(shopID,requesting,toRemove);
    }
    public boolean removeSubscribedUserFromSystem(SystemManager currUser, String userToRemoved) {
        return userController.removeSubscribedUserFromSystem(currUser,userToRemoved);
    }
    public Map<UserController.UserState, SubscribedUser> getSubscribedUserInfo(String userName){
        return userController.getSubscribedUserInfo(userName);
    }

    private static class FacadeHolder{
        private static Facade facade= new Facade();
    }
}
