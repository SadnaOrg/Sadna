package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;
import BusinessLayer.Shops.Polices.Discount.DiscountPred;
import BusinessLayer.Shops.Polices.Discount.DiscountRules;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscribedUserProxy extends UserProxy implements SubscribedUserBridge{
    SubscribedUserAdapter subscribedUserAdapter;
    public SubscribedUserProxy(UserProxy proxy){
        super(new SubscribedUserAdapter(proxy.getGuests(),proxy.getSubscribed()));
        subscribedUserAdapter = (SubscribedUserAdapter) super.adapter;
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
    public Integer createRelatedGroupDiscount(String username, Collection<Integer> relatedProducts, double discount, int connectId, int shopId) {
        return subscribedUserAdapter.createRelatedGroupDiscount(username,relatedProducts,discount,connectId,shopId);
    }

    @Override
    public Integer createShopDiscount(String username, int basketQuantity, double discount, int connectId, int shopId) {
        return subscribedUserAdapter.createShopDiscount(username,basketQuantity,discount,connectId,shopId);
    }

    @Override
    public Integer createDiscountAndPolicy(String username, DiscountPred discountPred, DiscountRules discountPolicy, int connectId, int shopId) {
        return null;
    }

    @Override
    public Integer createDiscountMaxPolicy(String username, DiscountRules discountPolicy, int connectId, int shopId) {
        return null;
    }

    @Override
    public Integer createDiscountOrPolicy(String username, DiscountPred discountPred, DiscountRules discountPolicy, int connectId, int shopId) {
        return null;
    }

    @Override
    public Integer createDiscountPlusPolicy(String username, DiscountRules discountPolicy, int connectId, int shopId) {
        return null;
    }

    @Override
    public Integer createDiscountXorPolicy(String username, DiscountRules discountRules1, DiscountRules discountRules2, DiscountPred tieBreaker, int connectId, int shopId) {
        return null;
    }
}
