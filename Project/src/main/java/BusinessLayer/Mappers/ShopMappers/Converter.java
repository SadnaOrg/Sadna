package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Shops.Polices.Discount.*;
import BusinessLayer.Shops.Polices.Purchase.*;
import com.SadnaORM.ShopImpl.ShopObjects.Discounts.DiscountPolicyDTO;
import com.SadnaORM.ShopImpl.ShopObjects.Discounts.DiscountPredDTO;
import com.SadnaORM.ShopImpl.ShopObjects.Policies.PurchasePolicyDTO;

public interface Converter {
    DiscountPolicyDTO convert(DiscountAndPolicy p);
    DiscountPolicyDTO convert(DiscountMaxPolicy p);
    DiscountPolicyDTO convert(DiscountOrPolicy p);
    DiscountPolicyDTO convert(DiscountPlusPolicy p);
    DiscountPolicyDTO convert(DiscountXorPolicy p);
    DiscountPolicyDTO convert(ProductByQuantityDiscount p);
    DiscountPolicyDTO convert(ProductDiscount p);
    DiscountPolicyDTO convert(ProductQuantityInPriceDiscount p);
    DiscountPolicyDTO convert(RelatedGroupDiscount p);
    DiscountPolicyDTO convert(ShopDiscount p);
    DiscountPredDTO convert(ValidateBasketQuantityDiscount p);
    DiscountPredDTO convert(ValidateBasketValueDiscount p);
    DiscountPredDTO convert(ValidateProductQuantityDiscount p);
    PurchasePolicyDTO convert(PurchaseAndPolicy p);
    PurchasePolicyDTO convert(PurchaseGriraPolicy p);
    PurchasePolicyDTO convert(PurchaseOrPolicy p);
    PurchasePolicyDTO convert(ValidateCategoryPurchase v);
    PurchasePolicyDTO convert(ValidateProductPurchase v);
    PurchasePolicyDTO convert(ValidateTImeStampPurchase v);
    PurchasePolicyDTO convert(ValidateUserPurchase v);
}
