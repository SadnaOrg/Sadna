package ServiceLayer.interfaces;

import BusinessLayer.Shops.Polices.Discount.DiscountPred;
import BusinessLayer.Shops.Polices.Discount.DiscountRules;
import ServiceLayer.Objects.*;
import ServiceLayer.Response;
import ServiceLayer.Result;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

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

    Response<Integer> createRelatedGroupDiscount(Collection<Integer> relatedProducts, double discount, int connectId , int shopId)  ;

    Response<Integer> createShopDiscount(int basketQuantity,double discount,int connectId, int shopId);

    Response<Integer> createDiscountAndPolicy(DiscountPred discountPred, DiscountRules discountPolicy, int connectId, int shopId) ;

    Response<Integer> createDiscountMaxPolicy(DiscountRules discountPolicy,int connectId, int shopId) ;

    Response<Integer>  createDiscountOrPolicy(DiscountPred discountPred,DiscountRules discountPolicy,int connectId, int shopId) ;

    Response<Integer>  createDiscountPlusPolicy(DiscountRules discountPolicy,int connectId, int shopId) ;

    Response<Integer> createDiscountXorPolicy(DiscountRules discountRules1, DiscountRules discountRules2,  DiscountPred tieBreaker,int connectId, int shopId)  ;

    Response<Integer>  createValidateBasketQuantityDiscount(int basketquantity, boolean cantBeMore ,int connectId, int shopId)  ;

    Response<Integer> createValidateBasketValueDiscount(double basketvalue ,boolean cantBeMore,int connectId, int shopId) ;

    Response<Integer> createValidateProductQuantityDiscount(int productId, int productQuantity, boolean cantbemore ,int connectId, int shopId) ;

    Response<Integer> createValidateProductPurchase(int productId, int productQuantity, boolean cantbemore, int connectId, int shopId);

    Response<Integer> createValidateTImeStampPurchase(LocalTime localTime, boolean buybefore, int conncectId, int shopId);

}