package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Polices.Discount.*;
import BusinessLayer.Shops.Shop;

import javax.naming.NoPermissionException;
import java.util.Collection;

public class SetPurchasePolicy extends BaseAction {

    private Shop shop;

    public SetPurchasePolicy(Shop shop) {
        this.shop= shop;
    }

    public boolean createProductByQuantityDiscount(int productId, int productQuantity, double discount,int conncectId) throws NoPermissionException {
        shop.addDiscount(conncectId, new ProductByQuantityDiscount(productId, productQuantity, discount));
        return true;
    }

    public boolean createProductDiscount(int productId, double discount,int conncectId) {
        shop.addDiscount(conncectId, new ProductDiscount(productId, discount));
        return true;
    }

    public boolean createProductQuantityInPriceDiscount(int productID, int quantity, double priceForQuantity,int conncectId)
    {
        shop.addDiscount(conncectId,new ProductQuantityInPriceDiscount(productID, quantity, priceForQuantity));
        return true;
    }
    public boolean createRelatedGroupDiscount(Collection<Integer> relatedProducts, double discount, int conncectId)
    {
        shop.addDiscount(conncectId,new RelatedGroupDiscount(relatedProducts, discount));
        return true;
    }
    public boolean createShopDiscount(int basketQuantity,double discount,int conncectId)
    {
        shop.addDiscount(conncectId,new ShopDiscount(basketQuantity, discount));
        return true;
    }
    public boolean createDiscountAndPolicy(DiscountPred discountPred,DiscountRules discountPolicy,int conncectId)
    {
        shop.addDiscount(conncectId,new DiscountAndPolicy(discountPred, discountPolicy));
        return true;
    }

    public boolean createDiscountMaxPolicy(DiscountRules discountPolicy,int conncectId)
    {
        shop.addDiscount(conncectId,new DiscountMaxPolicy(discountPolicy));
        return true;
    }

    public boolean createDiscountOrPolicy(DiscountPred discountPred,DiscountRules discountPolicy,int conncectId)
    {
        shop.addDiscount(conncectId,new DiscountOrPolicy(discountPred, discountPolicy));
        return true;
    }

    public boolean createDiscountPlusPolicy(DiscountRules discountPolicy,int conncectId)
    {
        shop.addDiscount(conncectId,new DiscountPlusPolicy(discountPolicy));
        return true;
    }

    public boolean createDiscountXorPolicy(DiscountRules discountRules1, DiscountRules discountRules2,  DiscountPred tieBreaker,int conncectId)
    {
        shop.addDiscount(conncectId,new DiscountXorPolicy(discountRules1,discountRules2,tieBreaker));
        return true;
    }

    public boolean createValidateBasketQuantityDiscount(int basketquantity,boolean cantBeMore ,int conncectId)
    {
        shop.addPredicate(conncectId,new ValidateBasketQuantityDiscount(basketquantity,cantBeMore));
        return true;
    }

    public boolean createValidateBasketValueDiscount(double basketvalue ,boolean cantBeMore,int conncectId)
    {
        shop.addPredicate(conncectId,new ValidateBasketValueDiscount(basketvalue,cantBeMore));
        return true;
    }

    public boolean createValidateProductQuantityDiscount(int productId, int productQuantity, boolean cantbemore ,int conncectId)
    {
        shop.addPredicate(conncectId,new ValidateProductQuantityDiscount(productId, productQuantity, cantbemore));
        return true;
    }
}
