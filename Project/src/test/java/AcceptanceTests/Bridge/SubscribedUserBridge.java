package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;
import BusinessLayer.Shops.Polices.Discount.DiscountPred;
import BusinessLayer.Shops.Polices.Discount.DiscountRules;
import ServiceLayer.Response;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SubscribedUserBridge extends UserBridge {

    Guest logout(String  userName);

    boolean updateProductQuantity(String username, int shopID, int productID ,int newQuantity);

    boolean updateProductPrice(String username, int shopID, int productID, double newPrice);

    boolean updateProductDescription(String username, int shopID, int productID, String Desc);

    boolean updateProductName(String username, int shopID, int productID, String newName);

    boolean deleteProductFromShop(String username, int shopID, int productID);

    boolean appointOwner(int shopID, String appointerName, String appointeeName);

    boolean appointManager(int shopID, String appointerName, String appointeeName);

    boolean closeShop(int shopID, String  userName);

    boolean changeAdminPermission(int shopID, String giverName, String receiverName, List<SubscribedUser.Permission> permission);

    Map<String, Appointment> getShopAppointments(String requestingUsername, int shopID);

    Map<String, List<SubscribedUser.Permission>> getShopPermissions(String requestingUsername, int shopID);

    boolean addProductToShop(String username,int shopID, Product product,int productID, int quantity, double price);

    Shop openShop(String username, String name, String desc);

    boolean removeAdmin(int shopID, String requesting, String toRemove);

    boolean reOpenShop(String username, int shopID);

    Integer createProductByQuantityDiscount(String username, int productId, int productQuantity, double discount, int connectId, int shopId);

    Integer createProductDiscount(String username, int productId, double discount, int connectId, int shopId) ;

    Integer createProductQuantityInPriceDiscount(String username, int productID, int quantity, double priceForQuantity, int connectId, int shopId)  ;

    Integer createRelatedGroupDiscount(String username, Collection<Integer> relatedProducts, double discount, int connectId , int shopId)  ;

    Integer createShopDiscount(String username, int basketQuantity,double discount,int connectId, int shopId);

    Integer createDiscountAndPolicy(String username, DiscountPred discountPred, DiscountRules discountPolicy, int connectId, int shopId) ;

    Integer createDiscountMaxPolicy(String username, DiscountRules discountPolicy,int connectId, int shopId) ;

    Integer  createDiscountOrPolicy(String username, DiscountPred discountPred,DiscountRules discountPolicy,int connectId, int shopId) ;

    Integer  createDiscountPlusPolicy(String username, DiscountRules discountPolicy,int connectId, int shopId) ;

    Integer createDiscountXorPolicy(String username, DiscountRules discountRules1, DiscountRules discountRules2,  DiscountPred tieBreaker,int connectId, int shopId)  ;

    Integer  createValidateBasketQuantityDiscount(String username,int basketquantity, boolean cantBeMore ,int connectId, int shopId)  ;

    Integer createValidateBasketValueDiscount(String username,double basketvalue ,boolean cantBeMore,int connectId, int shopId) ;

    Integer createValidateProductQuantityDiscount(String username,int productId, int productQuantity, boolean cantbemore ,int connectId, int shopId) ;

    Integer createValidateProductPurchase(String username,int productId, int productQuantity, boolean cantbemore, int connectId, int shopId);

    Integer createValidateTImeStampPurchase(String username,LocalTime localTime, boolean buybefore, int conncectId, int shopId);
}
