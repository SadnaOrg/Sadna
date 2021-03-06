package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Polices.Discount.*;
import BusinessLayer.Shops.Polices.Purchase.*;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BidOffer;

import javax.naming.NoPermissionException;
import java.time.LocalDate;
import java.time.LocalTime;

public class SetPurchasePolicy extends BaseAction {

    private Shop shop;

    public SetPurchasePolicy(Shop shop) {
        this.shop = shop;
    }

    public int createProductByQuantityDiscount(int productId, int productQuantity, double discount, int conncectId) throws NoPermissionException {
        return shop.addDiscount(conncectId, new ProductByQuantityDiscount(productId, productQuantity, discount));
    }

    public int createProductDiscount(int productId, double discount, int conncectId) {
        return shop.addDiscount(conncectId, new ProductDiscount(productId, discount));
    }

    public int createProductQuantityInPriceDiscount(int productID, int quantity, double priceForQuantity, int conncectId) {
        return shop.addDiscount(conncectId, new ProductQuantityInPriceDiscount(productID, quantity, priceForQuantity));
    }

    public int createRelatedGroupDiscount(String category, double discount, int conncectId) {
        return shop.addDiscount(conncectId, new RelatedGroupDiscount(category, discount));
    }

    public int createShopDiscount(int basketQuantity, double discount, int conncectId) {
        return shop.addDiscount(conncectId, new ShopDiscount(basketQuantity, discount));
    }

    public int createDiscountAndPolicy(DiscountPred discountPred, DiscountRules discountPolicy, int conncectId) {
        return shop.addDiscount(conncectId, new DiscountAndPolicy(discountPred, discountPolicy));
    }

    public int createDiscountMaxPolicy(DiscountRules discountPolicy, int conncectId) {
        return shop.addDiscount(conncectId, new DiscountMaxPolicy(discountPolicy));
    }

    public int createDiscountOrPolicy(DiscountPred discountPred, DiscountRules discountPolicy, int conncectId) {
        return shop.addDiscount(conncectId, new DiscountOrPolicy(discountPred, discountPolicy));
    }

    public int createDiscountPlusPolicy(DiscountRules discountPolicy, int conncectId) {
        return shop.addDiscount(conncectId, new DiscountPlusPolicy(discountPolicy));
    }

    public int createDiscountXorPolicy(DiscountRules discountRules1, DiscountRules discountRules2, DiscountPred tieBreaker, int conncectId) {
        return shop.addDiscount(conncectId, new DiscountXorPolicy(discountRules1, discountRules2, tieBreaker));
    }

    public int createValidateBasketQuantityDiscount(int basketquantity, boolean cantBeMore, int conncectId) {
        return shop.addPredicate(conncectId, new ValidateBasketQuantityDiscount(basketquantity, cantBeMore));
    }

    public int createValidateBasketValueDiscount(double basketvalue, boolean cantBeMore, int conncectId) {
        return shop.addPredicate(conncectId, new ValidateBasketValueDiscount(basketvalue, cantBeMore));
    }

    public int createValidateProductQuantityDiscount(int productId, int productQuantity, boolean cantbemore, int conncectId) {
        return shop.addPredicate(conncectId, new ValidateProductQuantityDiscount(productId, productQuantity, cantbemore));
    }

    public int createValidateProductPurchase(int productId, int productQuantity, boolean cantbemore, int conncectId) {
        return shop.addPurchasePolicy(conncectId,new ValidateProductPurchase(productId, productQuantity, cantbemore));
    }

    public int createValidateTimeStampPurchase(LocalTime localTime, boolean buybefore, int conncectId) {
        return shop.addPurchasePolicy(conncectId,new ValidateTImeStampPurchase(localTime, buybefore));
    }

    public int createValidateDateStampPurchase(LocalDate localDate, int conncectId) {
        return shop.addPurchasePolicy(conncectId,new ValidateTImeStampPurchase(localDate));
    }

    public int createValidateCategoryPurchase(String category, int productQuantity, boolean cantbemore, int conncectId) {
        return shop.addPurchasePolicy(conncectId, new ValidateCategoryPurchase(category,productQuantity,cantbemore));
    }

    public int createValidateUserPurchase(int age, int connectId) {
        return shop.addPurchasePolicy(connectId, new ValidateUserPurchase(age));
    }

    public int createPurchaseAndPolicy(PurchasePolicy policy, int conncectId) {
        return shop.addPurchasePolicy(conncectId, new PurchaseAndPolicy(policy));
    }

    public int createPurchaseOrPolicy(PurchasePolicy policy,int conncectId) {
        return shop.addPurchasePolicy(conncectId, new PurchaseOrPolicy(policy));
    }

    public boolean removeDiscount(int ID) {
        return shop.removeDiscount(ID);
    }

    public boolean removePredicate(int ID) {
        return shop.removePredicate(ID);
    }

    public boolean removePurchasePolicy(int purchasePolicyToDelete) {
        return shop.removePurchasePolicy(purchasePolicyToDelete);
    }

    public DiscountRules getDiscount() {
        return shop.getDiscounts();
    }

    public PurchasePolicy getPurchasePolicy() {
        return shop.getPurchasePolicy();
    }

    public boolean reOfferBid(String user,int productId, double newPrice)
    {
        return shop.reOfferBid(user, productId, newPrice);
    }

    public boolean declineBidOffer(String user,int productId)
    {
        return shop.declineBidOffer(user, productId);
    }

    public BidOffer approveBidOffer(String user, String adminName, int productId) {
        return shop.approveBidOffer(user, adminName, productId);
    }
}