package ServiceLayer.interfaces;

import ServiceLayer.Objects.*;
import ServiceLayer.Objects.Policies.Discount.DiscountPred;
import ServiceLayer.Objects.Policies.Discount.DiscountRules;
import ServiceLayer.Objects.Policies.Purchase.PurchasePolicy;
import ServiceLayer.Response;
import ServiceLayer.Result;

import javax.naming.NoPermissionException;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface SubscribedUserService extends UserService {

    Response<SystemManagerService> manageSystemAsSystemManager();

    Response<UserService> logout();

    Response<Shop> openShop(String name, String desc);

    Result assignShopManager(int shop, String userNameToAssign);

    Result assignShopOwner(int shop, String userNameToAssign);

    Result changeManagerPermission(int shop, String userNameToAssign, Collection<Integer> types);

    Result closeShop(int shop);

    Response<AdministratorInfo> getAdministratorInfo(int shop);

    Response<PurchaseHistoryInfo> getHistoryInfo(int shop);

    Result updateProductQuantity(int shopID, int productID ,int newQuantity);

    Result updateProductPrice(int shopID, int productID, double newPrice);

    Result updateProductDescription(int shopID, int productID, String Desc);

    Result updateProductName(int shopID, int productID, String newName);

    Result deleteProductFromShop(int shopID, int productID);

    Result addProductToShop(int shopID, String name, String desc,String manufacturer,int productID, int quantity, double price);

    Result reopenShop(int shopID);

    Response<SubscribedUser> getSubscribedUserInfo();

    Response<Administrator> getMyInfo(int shopID);

    Result removeAdmin(int shopID, String toRemove);

    Response<Integer> createProductByQuantityDiscount(int productId, int productQuantity, double discount, int connectId, int shopId);

    Response<Integer> createProductDiscount(int productId, double discount, int connectId, int shopId) ;

    Response<Integer> createProductQuantityInPriceDiscount(int productID, int quantity, double priceForQuantity, int connectId, int shopId)  ;

    Response<Integer> createRelatedGroupDiscount(String category, double discount, int connectId , int shopId)  ;

    Response<Integer> createShopDiscount(int basketQuantity,double discount,int connectId, int shopId);

    Response<Integer> createDiscountAndPolicy(ServiceLayer.Objects.Policies.Discount.DiscountPred discountPred, ServiceLayer.Objects.Policies.Discount.DiscountRules discountPolicy, int connectId, int shopId) ;

    Response<Integer> createDiscountAndPolicy(DiscountPred discountPred, int connectId, int shopId);

    Response<Integer> createDiscountMaxPolicy(ServiceLayer.Objects.Policies.Discount.DiscountRules discountPolicy, int connectId, int shopId)  ;

    Response<Integer> createDiscountMaxPolicy(int connectId, int shopId);

    Response<Integer>  createDiscountOrPolicy(ServiceLayer.Objects.Policies.Discount.DiscountPred discountPred, ServiceLayer.Objects.Policies.Discount.DiscountRules discountPolicy, int connectId, int shopId) ;

    Response<Integer> createDiscountOrPolicy(ServiceLayer.Objects.Policies.Discount.DiscountPred discountPred, int connectId, int shopId);

    Response<Integer>  createDiscountPlusPolicy(ServiceLayer.Objects.Policies.Discount.DiscountRules discountPolicy, int connectId, int shopId)  ;

    Response<Integer>  createDiscountPlusPolicy(int parentId, int shopId);

    Response<Integer> createDiscountXorPolicy(ServiceLayer.Objects.Policies.Discount.DiscountRules  discountRules1, ServiceLayer.Objects.Policies.Discount.DiscountRules  discountRules2, DiscountPred tieBreaker, int connectId, int shopId)  ;

    Response<Integer> createDiscountXorPolicy(DiscountPred tieBreaker, int connectId, int shopId);

    Response<Integer>  createValidateBasketQuantityDiscount(int basketquantity, boolean cantBeMore , int connectId, int shopId)  ;

    Response<Integer> createValidateBasketValueDiscount(double basketvalue ,boolean cantBeMore,int connectId, int shopId) ;

    Response<Integer> createValidateProductQuantityDiscount(int productId, int productQuantity, boolean cantbemore ,int connectId, int shopId) ;

    Response<Integer> createValidateProductPurchase(int productId, int productQuantity, boolean cantbemore, int connectId, int shopId);

    Response<Integer> createValidateTImeStampPurchase(LocalTime localTime, boolean buybefore, int conncectId, int shopId);

    Response<Integer> createValidateCategoryPurchase(String category, int productQuantity, boolean cantbemore, int connectId, int shopId);

    Response<Integer> createValidateUserPurchase(int age, int connectId, int shopId);

    Result setCategory(int productId, String category, int shopID);

    Result removeShopOwner(int shopId, String toRemove);

    Response<ShopsInfo> searchShops(Predicate<Shop> shopPred, String username);

    Response<Boolean> removeDiscount(int discountID, int shopId);

    Response<Boolean> removePredicate(int predicateID, int shopId);

    Response<Boolean> removePurchasePolicy(PurchasePolicy purchasePolicyToDelete, int shopId) ;

    Response<Integer> createPurchaseAndPolicy(PurchasePolicy policy, int conncectId, int shopId);

    Response<Integer> createPurchaseOrPolicy(PurchasePolicy policy, int conncectId, int shopId);

    Response<DiscountRules> getDiscount(int shopId);

    Response<PurchasePolicy> getPurchasePolicy(int shopId);
}
