package BusinessLayer.Mappers;

import BusinessLayer.Shops.Polices.Discount.*;
import BusinessLayer.Shops.Polices.Purchase.*;
import ORM.Shops.Discounts.DiscountPolicy;
import ORM.Shops.Discounts.DiscountPred;
import ORM.Shops.Discounts.ValidateBasketQuantityPred;
import ORM.Shops.Product;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

public class PolicyAndPurchasesConverter implements Converter{

    @Override
    public DiscountPolicy toEntity(DiscountAndPolicy p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        Collection<DiscountPred> discountPreds = p.getDiscountPreds().stream().map(pred -> pred.toEntity(this,shop)).toList();
        DiscountPolicy discountPolicy = p.getDiscountPolicy().toEntity(this, shop);
        return new ORM.Shops.Discounts.AndPolicy(shop, ID, discountPreds, discountPolicy);
    }

    @Override
    public DiscountPolicy toEntity(DiscountMaxPolicy p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        Collection<DiscountPolicy> discountPolicies = p.getDiscountPolicies().stream().map(policy -> p.toEntity(this,shop)).toList();
        return new ORM.Shops.Discounts.MaxDiscountPolicy(shop,ID, discountPolicies);
    }

    @Override
    public DiscountPolicy toEntity(DiscountOrPolicy p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        DiscountPolicy discountPolicy = p.getDiscountPolicy().toEntity(this,shop);
        Collection<DiscountPred> discountPreds = p.getDiscountPreds().stream().map(pred -> pred.toEntity(this,shop)).toList();
        return new ORM.Shops.Discounts.OrDiscount(shop, ID, discountPolicy, discountPreds);
    }

    @Override
    public DiscountPolicy toEntity(DiscountPlusPolicy p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        Collection<DiscountPolicy> discountPolicies = p.getDiscountPolicies().stream().map(policy -> p.toEntity(this,shop)).toList();
        return new ORM.Shops.Discounts.DiscountPlusPolicy(shop, ID, discountPolicies);
    }

    @Override
    public DiscountPolicy toEntity(DiscountXorPolicy p, ORM.Shops.Shop shop) {
        return null;
    }

    @Override
    public DiscountPolicy toEntity(ProductByQuantityDiscount p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        ORM.Shops.Product product = shop.getProducts().stream().filter(product1 -> p.getProductId()==ID).toList().get(0);
        int productQuantity = p.getProductQuantity();
        double discount = p.getDiscount();
        return new ORM.Shops.Discounts.ProductByQuantityDiscount(shop,ID,product,productQuantity,discount);
    }

    @Override
    public DiscountPolicy toEntity(ProductDiscount p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        Product product = shop.getProducts().stream().filter(product1 -> p.getProductId()==ID).toList().get(0);
        double discount = p.getDiscount();
        return new ORM.Shops.Discounts.ProductDiscount(shop,ID,product,discount);
    }

    @Override
    public DiscountPolicy toEntity(ProductQuantityInPriceDiscount p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        Product product = shop.getProducts().stream().filter(product1 -> p.getProductID()==ID).toList().get(0);
        int quantity = p.getQuantity();
        double priceForQuantity = p.getPriceForQuantity();
        return new ORM.Shops.Discounts.ProductQuantityInPriceDiscount(shop,ID,product,quantity,priceForQuantity);
    }

    @Override
    public DiscountPolicy toEntity(RelatedGroupDiscount p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        String category = p.getCategory();
        double discount = p.getDiscount();
        return new ORM.Shops.Discounts.RelatedGroupDiscount(shop,ID,category,discount);
    }

    @Override
    public ORM.Shops.Discounts.DiscountPolicy toEntity(ShopDiscount p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        int basketQuantity = p.getBasketQuantity();
        double discount = p.getDiscount();
        return new ORM.Shops.Discounts.ShopDiscount(shop,ID,basketQuantity,discount);
    }

    @Override
    public DiscountPred toEntity(ValidateBasketQuantityDiscount p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        int basketQuantity = p.getBasketquantity();
        boolean cantBeMore = p.isCantBeMore();
        return new ValidateBasketQuantityPred(shop,ID,basketQuantity,cantBeMore);
    }

    @Override
    public DiscountPred toEntity(ValidateBasketValueDiscount p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        double basketvalue = p.getBasketvalue();
        boolean cantBeMore = p.isCantBeMore();
        return new ORM.Shops.Discounts.ValidateBasketValueDiscount(shop,ID,basketvalue,cantBeMore);
    }

    @Override
    public DiscountPred toEntity(ValidateProductQuantityDiscount p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        Product product = shop.getProducts().stream().filter(product1 -> p.getProductId()==ID).toList().get(0);
        int productQuantity = p.getProductQuantity();
        boolean cantbemore = p.isCantbemore();
        return new ORM.Shops.Discounts.ValidateProductQuantityDiscount(shop,ID,product,productQuantity,cantbemore);
    }

    @Override
    public ORM.Shops.Purchases.PurchasePolicy toEntity(BusinessLayer.Shops.Polices.Purchase.PurchaseAndPolicy p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        Collection<ORM.Shops.Purchases.PurchasePolicy> policies = p.getPurchasePolicies().stream().map(purchasePolicy -> purchasePolicy.toEntity(this,shop)).toList();
        return new ORM.Shops.Purchases.PurchaseAndPolicy(shop,ID,policies);
    }

    @Override
    public ORM.Shops.Purchases.PurchasePolicy toEntity(PurchaseGriraPolicy p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        ORM.Shops.Purchases.PurchasePolicy purchasePolicyAllow = p.getPurchasePolicyAllow().toEntity(this,shop);
        ORM.Shops.Purchases.PurchasePolicy validatePurchase = p.getValidatePurchase().toEntity(this,shop);
        return new ORM.Shops.Purchases.PurchaseGriraPolicy(shop,ID,purchasePolicyAllow,validatePurchase);
    }

    @Override
    public ORM.Shops.Purchases.PurchasePolicy toEntity(PurchaseOrPolicy p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        Collection<ORM.Shops.Purchases.PurchasePolicy> policies = p.getPurchasePolicies().stream().map(purchasePolicy -> purchasePolicy.toEntity(this,shop)).toList();
        return new ORM.Shops.Purchases.PurchaseOrPolicy(shop,ID,policies);
    }

    @Override
    public ORM.Shops.Purchases.PurchasePolicy toEntity(ValidateCategoryPurchase p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        String category = p.getCategory();
        int productQuantity = p.getProductQuantity();
        boolean cantbemore = p.isCantbemore();
        return new ORM.Shops.Purchases.ValidateCategoryPurchase(shop,ID,category,productQuantity,cantbemore);
    }

    @Override
    public ORM.Shops.Purchases.PurchasePolicy toEntity(ValidateProductPurchase p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        int productID = p.getProductId();
        int productQuantity = p.getProductQuantity();
        boolean cantbemore = p.isCantbemore();
        return new ORM.Shops.Purchases.ValidateProductPurchase(shop,ID,productID,productQuantity,cantbemore);
    }

    @Override
    public ORM.Shops.Purchases.PurchasePolicy toEntity(ValidateTImeStampPurchase p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        LocalTime localTime = p.getLocalTime();
        LocalDate localDate = p.getDate();
        boolean buybefore = p.isBuybefore();
        return new ORM.Shops.Purchases.ValidateTImeStampPurchase(shop,ID,localTime,localDate,buybefore);
    }

    @Override
    public ORM.Shops.Purchases.PurchasePolicy toEntity(ValidateUserPurchase p, ORM.Shops.Shop shop) {
        int ID = p.getID();
        int age = p.getAge();
        return new ORM.Shops.Purchases.ValidateUserPurchase(shop,ID,age);
    }

    public PurchasePolicy fromPurchasePolicyEntity(ORM.Shops.Purchases.PurchasePolicy policyEntity){
        if(policyEntity instanceof ORM.Shops.Purchases.PurchaseAndPolicy){
            Collection<PurchasePolicy> purchasePolicies = ((ORM.Shops.Purchases.PurchaseAndPolicy) policyEntity).getPurchasePolicies().stream().map(this::fromPurchasePolicyEntity).toList();
            int policyLogicId = policyEntity.getID();
            return new PurchaseAndPolicy(purchasePolicies, policyLogicId);
        }
        if(policyEntity instanceof ORM.Shops.Purchases.PurchaseGriraPolicy){
            PurchasePolicy purchasePolicyAllow = fromPurchasePolicyEntity(((ORM.Shops.Purchases.PurchaseGriraPolicy) policyEntity).getPurchasePolicyAllow());
            PurchasePolicy validatePurchase = fromPurchasePolicyEntity(((ORM.Shops.Purchases.PurchaseGriraPolicy) policyEntity).getValidatePurchase());
            return new PurchaseGriraPolicy(purchasePolicyAllow,validatePurchase);
        }
        if(policyEntity instanceof ORM.Shops.Purchases.PurchaseOrPolicy){
            Collection<PurchasePolicy> purchasePolicies = ((ORM.Shops.Purchases.PurchaseOrPolicy) policyEntity).getPurchasePolicies().stream().map(this::fromPurchasePolicyEntity).toList();
            int policyLogicId = policyEntity.getID();
            return new PurchaseOrPolicy(purchasePolicies, policyLogicId);
        }
        if(policyEntity instanceof ORM.Shops.Purchases.ValidateCategoryPurchase){
            int policyLogicId = policyEntity.getID();
            String category = ((ORM.Shops.Purchases.ValidateCategoryPurchase) policyEntity).getCategory();
            int productQuantity = ((ORM.Shops.Purchases.ValidateCategoryPurchase) policyEntity).getProductQuantity();
            boolean cantbemore = ((ORM.Shops.Purchases.ValidateCategoryPurchase) policyEntity).isCantbemore();
            return new ValidateCategoryPurchase(policyLogicId,category,productQuantity,cantbemore);
        }
        if(policyEntity instanceof ORM.Shops.Purchases.ValidateProductPurchase){
            int policyLogicId = policyEntity.getID();
            int productId = ((ORM.Shops.Purchases.ValidateProductPurchase) policyEntity).getProductId();
            int productQuantity = ((ORM.Shops.Purchases.ValidateProductPurchase) policyEntity).getProductQuantity();
            boolean cantbemore = ((ORM.Shops.Purchases.ValidateProductPurchase) policyEntity).isCantbemore();
            return new ValidateProductPurchase(policyLogicId,productId,productQuantity,cantbemore);
        }
        if(policyEntity instanceof ORM.Shops.Purchases.ValidateTImeStampPurchase){
            int policyLogicId = policyEntity.getID();
            LocalTime localTime = ((ORM.Shops.Purchases.ValidateTImeStampPurchase) policyEntity).getLocalTime();
            LocalDate date = ((ORM.Shops.Purchases.ValidateTImeStampPurchase) policyEntity).getDate();
            boolean buybefore = ((ORM.Shops.Purchases.ValidateTImeStampPurchase) policyEntity).isBuybefore();
            return new ValidateTImeStampPurchase(policyLogicId,localTime,date,buybefore);
        }
        if(policyEntity instanceof  ORM.Shops.Purchases.ValidateUserPurchase){
            int policyLogicId = policyEntity.getID();
            int age = ((ORM.Shops.Purchases.ValidateUserPurchase) policyEntity).getAge();
            return new ValidateUserPurchase(policyLogicId, age);
        }
        throw new IllegalArgumentException("unknown policy type");
    }

    public BusinessLayer.Shops.Polices.Discount.DiscountPred fromDiscountPredicateEntity(ORM.Shops.Discounts.DiscountPred policyEntity){
        throw new IllegalArgumentException("unknown discount pred type");
    }

    public BusinessLayer.Shops.Polices.Discount.DiscountPolicy fromDiscountPolicyEntity(ORM.Shops.Discounts.DiscountPolicy policy){
        throw new IllegalArgumentException("unknown discount policy type");
    }
}
