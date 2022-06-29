package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;
import ServiceLayer.Objects.Policies.Discount.DiscountPred;
import ServiceLayer.Objects.Policies.Discount.DiscountRules;
import ServiceLayer.interfaces.SystemManagerService;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class SubscribedUserProxy extends UserProxy implements SubscribedUserBridge{
    SubscribedUserBridge subscribedUserAdapter;
    public SubscribedUserProxy(UserProxy proxy){
        super(new SubscribedUserAdapter(proxy.getGuests(),proxy.getSubscribed(),proxy.getNotifications()));
        subscribedUserAdapter = (SubscribedUserAdapter) super.adapter;
    }

    public SubscribedUserProxy(SystemManagerBridge adapter){
        super(adapter);
        subscribedUserAdapter = adapter;
    }

    @Override
    public Guest logout(String userName) {
        return subscribedUserAdapter.logout(userName);
    }

    @Override
    public boolean updateProductQuantity(String username, int shopID, int productID, int newQuantity) {
        return subscribedUserAdapter.updateProductQuantity(username,shopID,productID,newQuantity);
    }

    @Override
    public boolean updateProductPrice(String username, int shopID, int productID, double newPrice) {
        return subscribedUserAdapter.updateProductPrice(username,shopID,productID,newPrice);
    }

    @Override
    public boolean updateProductDescription(String username, int shopID, int productID, String Desc) {
        return subscribedUserAdapter.updateProductDescription(username,shopID,productID,Desc);
    }

    @Override
    public boolean updateProductName(String username, int shopID, int productID, String newName) {
        return subscribedUserAdapter.updateProductName(username,shopID,productID,newName);
    }

    @Override
    public boolean deleteProductFromShop(String username, int shopID, int productID) {
        return subscribedUserAdapter.deleteProductFromShop(username,shopID,productID);
    }

    @Override
    public boolean appointOwner(int shopID, String appointerName, String appointeeName) {
        return subscribedUserAdapter.appointOwner(shopID,appointerName,appointeeName);
    }

    @Override
    public boolean appointManager(int shopID, String appointerName, String appointeeName) {
        return subscribedUserAdapter.appointManager(shopID,appointerName,appointeeName);
    }

    @Override
    public boolean closeShop(int shopID, String userName) {
        return subscribedUserAdapter.closeShop(shopID,userName);
    }

    @Override
    public boolean changeAdminPermission(int shopID, String giverName, String receiverName, List<SubscribedUser.Permission> permission) {
        return subscribedUserAdapter.changeAdminPermission(shopID,giverName,receiverName,permission);
    }

    @Override
    public Map<String, Appointment> getShopAppointments(String requestingUsername, int shopID) {
        return subscribedUserAdapter.getShopAppointments(requestingUsername,shopID);
    }

    @Override
    public Map<String, List<SubscribedUser.Permission>> getShopPermissions(String requestingUsername, int shopID) {
        return subscribedUserAdapter.getShopPermissions(requestingUsername,shopID);
    }

    @Override
    public boolean addProductToShop(String username, int shopID, Product product, int productID, int quantity, double price) {
        return subscribedUserAdapter.addProductToShop(username,shopID,product,productID,quantity,price);
    }

    @Override
    public Shop openShop(String username, String name, String desc) {
        return subscribedUserAdapter.openShop(username,name,desc);
    }

    @Override
    public boolean removeAdmin(int shopID, String requesting, String toRemove) {
        return subscribedUserAdapter.removeAdmin(shopID,requesting,toRemove);
    }

    @Override
    public boolean reOpenShop(String username, int shopID){
        return subscribedUserAdapter.reOpenShop(username,shopID);
    }

    @Override
    public Integer createProductByQuantityDiscount(String username, int productId, int productQuantity, double discount, int connectId, int shopId) {
        return subscribedUserAdapter.createProductByQuantityDiscount(username,productId,productQuantity,discount,connectId,shopId);
    }

    @Override
    public Integer createProductDiscount(String username, int productId, double discount, int connectId, int shopId) {
        return subscribedUserAdapter.createProductDiscount(username,productId,discount,connectId,shopId);
    }

    @Override
    public Integer createProductQuantityInPriceDiscount(String username, int productID, int quantity, double priceForQuantity, int connectId, int shopId) {
        return subscribedUserAdapter.createProductQuantityInPriceDiscount(username,productID,quantity,priceForQuantity,connectId,shopId);
    }

    @Override
    public Integer createRelatedGroupDiscount(String username, String category, double discount, int connectId, int shopId) {
        return subscribedUserAdapter.createRelatedGroupDiscount(username,category,discount,connectId,shopId);
    }

    @Override
    public Integer createShopDiscount(String username, int basketQuantity, double discount, int connectId, int shopId) {
        return subscribedUserAdapter.createShopDiscount(username,basketQuantity,discount,connectId,shopId);
    }

    @Override
    public Integer createDiscountAndPolicy(String username,DiscountPred discountPred, DiscountRules discountPolicy, int connectId, int shopId) {
        return subscribedUserAdapter.createDiscountAndPolicy(username,discountPred,discountPolicy,connectId,shopId);
    }

    @Override
    public Integer createDiscountMaxPolicy(String username,DiscountRules discountPolicy, int connectId, int shopId) {
        return subscribedUserAdapter.createDiscountMaxPolicy(username,discountPolicy,connectId,shopId);
    }

    @Override
    public Integer createDiscountOrPolicy(String username,DiscountPred discountPred, DiscountRules discountPolicy, int connectId, int shopId) {
        return subscribedUserAdapter.createDiscountOrPolicy(username,discountPred,discountPolicy,connectId,shopId);
    }

    @Override
    public Integer createDiscountPlusPolicy(String username,DiscountRules discountPolicy, int connectId, int shopId) {
        return subscribedUserAdapter.createDiscountPlusPolicy(username,discountPolicy,connectId,shopId);
    }

    @Override
    public Integer createDiscountXorPolicy(String username,DiscountRules discountRules1, DiscountRules discountRules2, DiscountPred tieBreaker, int connectId, int shopId) {
        return subscribedUserAdapter.createDiscountXorPolicy(username,discountRules1,discountRules2,tieBreaker,connectId,shopId);
    }

    @Override
    public Integer createValidateBasketQuantityDiscount(String username, int basketquantity, boolean cantBeMore, int connectId, int shopId) {
        return subscribedUserAdapter.createValidateBasketQuantityDiscount(username,basketquantity,cantBeMore,connectId,shopId);
    }

    @Override
    public Integer createValidateBasketValueDiscount(String username, double basketvalue, boolean cantBeMore, int connectId, int shopId) {
        return subscribedUserAdapter.createValidateBasketValueDiscount(username,basketvalue,cantBeMore,connectId,shopId);
    }

    @Override
    public Integer createValidateProductQuantityDiscount(String username, int productId, int productQuantity, boolean cantbemore, int connectId, int shopId) {
        return subscribedUserAdapter.createValidateProductQuantityDiscount(username,productId,productQuantity,cantbemore,connectId,shopId);
    }

    @Override
    public Integer createValidateProductPurchase(String username, int productId, int productQuantity, boolean cantbemore, int connectId, int shopId) {
        return subscribedUserAdapter.createValidateProductPurchase(username,productId,productQuantity,cantbemore,connectId,shopId);
    }

    @Override
    public Integer createValidateTImeStampPurchase(String username, LocalTime localTime, boolean buybefore, int conncectId, int shopId) {
        return subscribedUserAdapter.createValidateTImeStampPurchase(username, localTime, buybefore, conncectId, shopId);
    }

    @Override
    public Integer createValidateCategoryPurchase(String username,String category, int productQuantity, boolean cantbemore, int connectId, int shopId) {
        return subscribedUserAdapter.createValidateCategoryPurchase(username,category,productQuantity,cantbemore,connectId,shopId);
    }

    @Override
    public Integer createValidateUserPurchase(String username, int age, int connectId, int shopId) {
        return subscribedUserAdapter.createValidateUserPurchase(username,age,connectId,shopId);
    }

    @Override
    public boolean removePurchasePolicy(String username, int purchasePolicyToDelete, int shopId) {
        return subscribedUserAdapter.removePurchasePolicy(username,purchasePolicyToDelete,shopId);
    }

    @Override
    public boolean removeDiscount(String username,int discountID, int shopId) {
        return subscribedUserAdapter.removeDiscount(username,discountID,shopId);
    }

    @Override
    public boolean removePredicate(String username,int predicateID, int shopId) {
        return subscribedUserAdapter.removePredicate(username,predicateID,shopId);
    }

    @Override
    public boolean setCategory(String username, int productId, String category, int shopID) {
        return subscribedUserAdapter.setCategory(username,productId,category,shopID);
    }

    @Override
    public boolean approveHeskemMinui(String username, int shop, String adminToAssign) {
        return subscribedUserAdapter.approveHeskemMinui(username,shop,adminToAssign);
    }

    @Override
    public boolean declineHeskemMinui(String username, int shop, String adminToAssign) {
        return subscribedUserAdapter.declineHeskemMinui(username,shop,adminToAssign);
    }

    @Override
    public SystemManager manageSystemAsSystemManager(String username) {
        return subscribedUserAdapter.manageSystemAsSystemManager(username);
    }

    @Override
    public boolean reOfferBid(String username,String user, int productId, double newPrice, int shopId) {
        return subscribedUserAdapter.reOfferBid(username,user,productId,newPrice,shopId);
    }

    @Override
    public boolean declineBidOffer(String username,String user, int productId, int shopId) {
        return subscribedUserAdapter.declineBidOffer(username,user,productId,shopId);
    }

    @Override
    public boolean approveBidOffer(String username,String user, int productId, int shopId) {
        return subscribedUserAdapter.approveBidOffer(username,user,productId,shopId);
    }
}
