package BusinessLayer;

import BusinessLayer.Notifications.ConcreteNotification;
import BusinessLayer.Notifications.Notification;
import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Shops.Polices.Discount.DiscountPred;
import BusinessLayer.Shops.Polices.Discount.DiscountRules;
import BusinessLayer.Shops.Polices.Purchase.PurchasePolicy;
import BusinessLayer.Users.*;
import BusinessLayer.Shops.*;
import BusinessLayer.System.PaymentMethod;
import BusinessLayer.System.System;

import javax.naming.NoPermissionException;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Facade{
    private final UserController userController = UserController.getInstance();
    private final ShopController shopController= ShopController.getInstance();
    private final System system = System.getInstance();

    private Facade() {
    }

    public static Facade getInstance(){
        return FacadeHolder.facade;
    }

    public ConcurrentHashMap<Integer, ShopInfo> reciveInformation() {
        return userController.reciveInformation();
    }

    public Guest logout(User currUser) {
        var g= userController.logout(currUser.getUserName());
        if(g!= null)
            system.getNotifier().unregister(currUser.getUserName());
        return g;
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
        return notifyUsers(userController.changeManagerPermission(currUser,shop,userNameToAssign,types),
                toCollection(userNameToAssign),
                currUser.getUserName()+" has change your permission as administrator of shop "+shop+" to"+types.toString());
    }

    public boolean closeShop(SubscribedUser currUser, int shop) throws NoPermissionException {
        return  notifyUsers(userController.closeShop(currUser,shop),
                getSystemManagersAndShopAdmins(shop),
                currUser.getUserName()+" has close the shop "+shop);
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

    public ConcurrentHashMap<Integer, ShopInfo> searchShops(ShopFilters shopPred, String username){
        return shopController.searchShops(shopPred, username);
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

// TODO: fix
    public double purchaseCartFromShop(User currUser, PaymentMethod method) {
        ConcurrentHashMap<Integer, Double> prices = shopController.purchaseBasket(currUser);
        ConcurrentHashMap<Integer, Boolean> paymentSituation = system.pay(prices, method);
        if(paymentSituation.containsValue(false))
            return 0;
        shopController.addToPurchaseHistory(currUser.getName(), paymentSituation);
        return prices.values().stream().reduce(0.0, Double::sum);
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

    public boolean registerToNotifier(String userName, Function<Notification, Boolean> con){
        system.getNotifier().register(con,userName);
        return true;
    }
    public boolean sendNotification(Collection<String> users, String Content){
        system.getNotifier().addNotification(new ConcreteNotification(users,Content));
        return true;
    }
    public boolean sendNotification(Notification not){
        system.getNotifier().addNotification(not);
        return true;
    }

    public boolean getDelayedNotifications(User currUser){
        system.getNotifier().getDelayedNotifications(currUser.getUserName());
        return true;
    }

    public boolean updateProductQuantity(String username,int shopID, int productID, int newQuantity) throws NoPermissionException {
        return notifyUsers(userController.updateProductQuantity(username,shopID,productID,newQuantity),
                getShopAdmins(shopID),
                "product "+productID+" from shop "+shopID+" quantity has change to: "+newQuantity);
    }

    public boolean updateProductPrice(String username,int shopID, int productID, double newPrice) throws NoPermissionException {
        return notifyUsers(userController.updateProductPrice(username,shopID,productID,newPrice),
                getShopAdmins(shopID),
                "product "+productID+" from shop "+shopID+" price has change to: "+newPrice);
    }

    public boolean updateProductDescription(String username,int shopID, int productID, String Desc) throws NoPermissionException {
        return notifyUsers(userController.updateProductDescription(username,shopID,productID,Desc),
                getShopAdmins(shopID),
                "product "+productID+" from shop "+shopID+" description has change to: "+Desc);
    }

    public boolean updateProductName(String username,int shopID, int productID, String newName) throws NoPermissionException {
        return notifyUsers(userController.updateProductName(username,shopID,productID,newName),
                getShopAdmins(shopID),
                "product "+productID+" from shop "+shopID+" name has change to "+newName);
    }

    public boolean deleteProductFromShop(String username,int shopID, int productID) throws NoPermissionException {
        return notifyUsers(userController.deleteProductFromShop(username,shopID,productID),
                getShopAdmins(shopID),
                "product "+productID+" from shop "+shopID+" got deleted");
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
        return  notifyUsers(userController.removeAdmin(shopID,requesting,toRemove),
                toCollection(toRemove),
                requesting+": has remove u as administrating the shop "+shopID);
    }
    public boolean removeSubscribedUserFromSystem(SystemManager currUser, String userToRemoved) {
        return userController.removeSubscribedUserFromSystem(currUser,userToRemoved);
    }
    public Map<UserController.UserState, List<SubscribedUser>> getSubscribedUserInfo(String userName){
        return userController.getSubscribedUserInfo(userName);
    }

    public Boolean removeShopOwner(int shopID, String requesting, String toRemove) throws NoPermissionException {
        return userController.removeShopOwner(shopID, requesting, toRemove);
    }

    private static class FacadeHolder{
        private static final Facade facade= new Facade();
    }

    private <T> T notifyUsers(T toReturn,Collection<String> users,String content){
        system.getNotifier().addNotification(new ConcreteNotification(users,content));
        return toReturn;
    }

    private Collection<String> toCollection(String s){
        var x = new ConcurrentLinkedDeque<String>();
        x.add(s);
        return x;
    }
    private Collection<String> getFounder(int shopID){
        return toCollection(shopController.getShops().get(shopID).getFounder().getUserName());
    }
    private Collection<String> getOwners(int shopID){
        return shopController.getShops().get(shopID).getShopAdministrators().stream().filter(s->s instanceof ShopOwner).map(ShopAdministrator::getUserName).collect(Collectors.toList());
    }
    private Collection<String> getShopAdmins(int shopID){
        return shopController.getShops().get(shopID).getShopAdministrators().stream().map(ShopAdministrator::getUserName).collect(Collectors.toList());
    }
    private Collection<String> getSystemManagers(){
        return userController.getSysManagers().stream().map(User::getUserName).collect(Collectors.toList());
    }
    private Collection<String> getSystemManagersAndShopAdmins(int shopID){
        var c = getShopAdmins(shopID);
        c.addAll(getSystemManagers());
        return c;
    }


    public int createProductByQuantityDiscount(SubscribedUser currUser, int productId, int productQuantity, double discount, int connectId, int shopId) throws NoPermissionException {
        return userController.createProductByQuantityDiscount(currUser,productId, productQuantity, discount, connectId, shopId);
    }

    public int createProductDiscount(SubscribedUser currUser, int productId, double discount, int connectId, int shopId) throws NoPermissionException {
        return userController.createProductDiscount(currUser,productId, discount, connectId, shopId);
    }

    public int createProductQuantityInPriceDiscount(SubscribedUser currUser, int productID, int quantity, double priceForQuantity, int connectId, int shopId) throws NoPermissionException {
        return userController.createProductQuantityInPriceDiscount(currUser,productID, quantity, priceForQuantity, connectId, shopId);
    }

    public int createRelatedGroupDiscount(SubscribedUser currUser, Collection<Integer> relatedProducts, double discount, int connectId , int shopId) throws NoPermissionException {
        return userController.createRelatedGroupDiscount(currUser,relatedProducts, discount, connectId, shopId);
    }

    public int createShopDiscount(SubscribedUser currUser, int basketQuantity,double discount,int connectId, int shopId) throws NoPermissionException {

        return userController.createShopDiscount(currUser,basketQuantity, discount, connectId, shopId);
    }

    public int createDiscountAndPolicy(SubscribedUser currUser, DiscountPred discountPred, DiscountRules discountPolicy, int connectId, int shopId) throws NoPermissionException {
        return userController.createDiscountAndPolicy(currUser,discountPred, discountPolicy, connectId, shopId);
    }

    public int createDiscountMaxPolicy(SubscribedUser currUser, DiscountRules discountPolicy,int connectId, int shopId) throws NoPermissionException {
        return userController.createDiscountMaxPolicy(currUser,discountPolicy, connectId, shopId);
    }

    public int  createDiscountOrPolicy(SubscribedUser currUser, DiscountPred discountPred,DiscountRules discountPolicy,int connectId, int shopId) throws NoPermissionException {
        return userController.createDiscountOrPolicy(currUser,discountPred, discountPolicy, connectId, shopId);
    }

    public int  createDiscountPlusPolicy(SubscribedUser currUser, DiscountRules discountPolicy,int connectId, int shopId) throws NoPermissionException {
        return userController.createDiscountPlusPolicy(currUser,discountPolicy, connectId, shopId);
    }

    public int createDiscountXorPolicy(SubscribedUser currUser, DiscountRules discountRules1, DiscountRules discountRules2,  DiscountPred tieBreaker,int connectId, int shopId) throws NoPermissionException {
        return userController.createDiscountXorPolicy(currUser,discountRules1, discountRules2, tieBreaker, connectId, shopId);
    }

    public int  createValidateBasketQuantityDiscount(SubscribedUser currUser, int basketquantity, boolean cantBeMore ,int connectId, int shopId) throws NoPermissionException {
        return userController.createValidateBasketQuantityDiscount(currUser,basketquantity, cantBeMore, connectId, shopId);
    }

    public int createValidateBasketValueDiscount(SubscribedUser currUser, double basketvalue ,boolean cantBeMore,int connectId, int shopId) throws NoPermissionException {
        return userController.createValidateBasketValueDiscount(currUser,basketvalue, cantBeMore, connectId, shopId);
    }
    public int createValidateProductQuantityDiscount(SubscribedUser currUser, int productId, int productQuantity, boolean cantbemore ,int connectId, int shopId) throws NoPermissionException {

        return userController.createValidateProductQuantityDiscount(currUser,productId, productQuantity, cantbemore, connectId, shopId);
    }
    public int createValidateProductPurchase(SubscribedUser currUser,int productId, int productQuantity, boolean cantbemore, int connectId, int shopId) throws NoPermissionException {
        return userController.createValidateProductPurchase(currUser, productId, productQuantity, cantbemore, connectId, shopId);

    }
    public int createValidateTImeStampPurchase(SubscribedUser currUser, LocalTime localTime, boolean buybefore, int conncectId, int shopId) throws NoPermissionException {
        return userController.createValidateTImeStampPurchase(currUser, localTime,buybefore,conncectId,shopId);
    }

    public int createPurchaseAndPolicy(SubscribedUser currUser,PurchasePolicy policy, int conncectId, int shopId) throws NoPermissionException {
        return userController.createPurchaseAndPolicy(currUser,policy, conncectId, shopId);
    }

    public int createPurchaseOrPolicy(SubscribedUser currUser,PurchasePolicy policy, int conncectId, int shopId) throws NoPermissionException {
        return userController.createPurchaseOrPolicy(currUser,policy, conncectId, shopId);
    }


    public boolean removeDiscount(SubscribedUser currUser,DiscountRules discountRules, int shopId) throws NoPermissionException {
        return userController.removeDiscount(currUser,discountRules,shopId);
    }

    public boolean removePredicate(SubscribedUser currUser,DiscountPred discountPred, int shopId) throws NoPermissionException {
        return userController.removePredicate(currUser,discountPred,shopId);
    }
    public boolean removePurchasePolicy(SubscribedUser currUser, PurchasePolicy purchasePolicyToDelete, int shopId) throws NoPermissionException {
        return userController.removePurchasePolicy(currUser, purchasePolicyToDelete, shopId);
    }


}


