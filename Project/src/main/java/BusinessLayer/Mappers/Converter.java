package BusinessLayer.Mappers;

import BusinessLayer.Shops.Polices.Discount.*;
import BusinessLayer.Shops.Polices.Purchase.*;

public interface Converter {
    ORM.Shops.Discounts.DiscountPolicy toEntity(DiscountAndPolicy p, ORM.Shops.Shop shop);

    ORM.Shops.Discounts.DiscountPolicy toEntity(DiscountMaxPolicy p, ORM.Shops.Shop shop);

    ORM.Shops.Discounts.DiscountPolicy toEntity(DiscountOrPolicy p, ORM.Shops.Shop shop);

    ORM.Shops.Discounts.DiscountPolicy toEntity(DiscountPlusPolicy p, ORM.Shops.Shop shop);

    ORM.Shops.Discounts.DiscountPolicy toEntity(DiscountXorPolicy p, ORM.Shops.Shop shop);

    ORM.Shops.Discounts.DiscountPolicy toEntity(ProductByQuantityDiscount p, ORM.Shops.Shop shop);

    ORM.Shops.Discounts.DiscountPolicy toEntity(ProductDiscount p, ORM.Shops.Shop shop);

    ORM.Shops.Discounts.DiscountPolicy toEntity(ProductQuantityInPriceDiscount p, ORM.Shops.Shop shop);

    ORM.Shops.Discounts.DiscountPolicy toEntity(RelatedGroupDiscount p, ORM.Shops.Shop shop);

    ORM.Shops.Discounts.DiscountPolicy toEntity(ShopDiscount p, ORM.Shops.Shop shop);

    ORM.Shops.Discounts.DiscountPred toEntity(ValidateBasketQuantityDiscount p, ORM.Shops.Shop shop);

    ORM.Shops.Discounts.DiscountPred toEntity(ValidateBasketValueDiscount p, ORM.Shops.Shop shop);

    ORM.Shops.Discounts.DiscountPred toEntity(ValidateProductQuantityDiscount p, ORM.Shops.Shop shop);

    ORM.Shops.Purchases.PurchasePolicy toEntity(PurchaseAndPolicy p, ORM.Shops.Shop shop);

    ORM.Shops.Purchases.PurchasePolicy toEntity(PurchaseGriraPolicy p, ORM.Shops.Shop shop);

    ORM.Shops.Purchases.PurchasePolicy toEntity(PurchaseOrPolicy p, ORM.Shops.Shop shop);

    ORM.Shops.Purchases.PurchasePolicy toEntity(ValidateCategoryPurchase p, ORM.Shops.Shop shop);

    ORM.Shops.Purchases.PurchasePolicy toEntity(ValidateProductPurchase p, ORM.Shops.Shop shop);

    ORM.Shops.Purchases.PurchasePolicy toEntity(ValidateTImeStampPurchase p, ORM.Shops.Shop shop);

    ORM.Shops.Purchases.PurchasePolicy toEntity(ValidateUserPurchase p, ORM.Shops.Shop shop);
}
