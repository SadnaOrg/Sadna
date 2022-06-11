package ServiceLayer.Objects.Policies.Discount;

public interface DiscountPred {

//    static BusinessLayer.Shops.Polices.Discount.DiscountPred makeBusinessPred(DiscountPred discountPred)
//    {
//        if(discountPred instanceof ValidateBasketQuantityDiscount)
//        {
//            return new BusinessLayer.Shops.Polices.Discount.ValidateBasketQuantityDiscount();
//        }
//    }

    static DiscountPred makeServicePred(BusinessLayer.Shops.Polices.Discount.DiscountPred discountPred)
    {
        if(discountPred instanceof BusinessLayer.Shops.Polices.Discount.ValidateBasketQuantityDiscount)
        {
            return new ValidateBasketQuantityDiscount((BusinessLayer.Shops.Polices.Discount.ValidateBasketQuantityDiscount)discountPred);
        }
        if(discountPred instanceof BusinessLayer.Shops.Polices.Discount.ValidateBasketValueDiscount)
        {
            return new ValidateBasketValueDiscount((BusinessLayer.Shops.Polices.Discount.ValidateBasketValueDiscount)discountPred);
        }
        if(discountPred instanceof BusinessLayer.Shops.Polices.Discount.ValidateProductQuantityDiscount)
        {
            return new ValidateProductQuantityDiscount((BusinessLayer.Shops.Polices.Discount.ValidateProductQuantityDiscount)discountPred);
        }
        throw new IllegalStateException("can't be other then the known ones");
    }
}
