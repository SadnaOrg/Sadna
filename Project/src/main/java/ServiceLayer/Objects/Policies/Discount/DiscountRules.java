package ServiceLayer.Objects.Policies.Discount;

public interface DiscountRules{

    static BusinessLayer.Shops.Polices.Discount.DiscountRules makeBusinessRule(DiscountRules discountRules) {
        throw new IllegalStateException("can't be other then the known ones");
    }

    static DiscountRules makeServiceRule(BusinessLayer.Shops.Polices.Discount.DiscountRules discountRules)
    {
        if(discountRules instanceof BusinessLayer.Shops.Polices.Discount.ProductByQuantityDiscount)
        {
            return new ProductByQuantityDiscount((BusinessLayer.Shops.Polices.Discount.ProductByQuantityDiscount)discountRules);
        }
        if(discountRules instanceof BusinessLayer.Shops.Polices.Discount.ProductDiscount)
        {
            return new ProductDiscount((BusinessLayer.Shops.Polices.Discount.ProductDiscount)discountRules);
        }
        if(discountRules instanceof BusinessLayer.Shops.Polices.Discount.ProductQuantityInPriceDiscount)
        {
            return new ProductQuantityInPriceDiscount((BusinessLayer.Shops.Polices.Discount.ProductQuantityInPriceDiscount)discountRules);
        }
        if(discountRules instanceof BusinessLayer.Shops.Polices.Discount.ShopDiscount)
        {
            return new ShopDiscount((BusinessLayer.Shops.Polices.Discount.ShopDiscount)discountRules);
        }
        if(discountRules instanceof BusinessLayer.Shops.Polices.Discount.DiscountAndPolicy)
        {
            return new DiscountAndPolicy((BusinessLayer.Shops.Polices.Discount.DiscountAndPolicy)discountRules);
        }
        if(discountRules instanceof BusinessLayer.Shops.Polices.Discount.DiscountOrPolicy)
        {
            return new DiscountOrPolicy((BusinessLayer.Shops.Polices.Discount.DiscountOrPolicy)discountRules);
        }
        if(discountRules instanceof BusinessLayer.Shops.Polices.Discount.DiscountXorPolicy)
        {
            return new DiscountXorPolicy((BusinessLayer.Shops.Polices.Discount.DiscountXorPolicy)discountRules);
        }
        if(discountRules instanceof BusinessLayer.Shops.Polices.Discount.DiscountMaxPolicy)
        {
            return new DiscountMaxPolicy((BusinessLayer.Shops.Polices.Discount.DiscountMaxPolicy)discountRules);
        }
        if(discountRules instanceof BusinessLayer.Shops.Polices.Discount.DiscountPlusPolicy)
        {
            return new DiscountPlusPolicy((BusinessLayer.Shops.Polices.Discount.DiscountPlusPolicy)discountRules);
        }
        throw new IllegalStateException("can't be other then the known ones");
    }
}
