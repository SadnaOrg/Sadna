package ServiceLayer.Objects.Policies.Discount;


public record ValidateProductQuantityDiscount(int ruleId, int productId, int productQuantity, boolean cantbemore) implements DiscountPred{

    public ValidateProductQuantityDiscount(BusinessLayer.Shops.Polices.Discount.ValidateProductQuantityDiscount validateProductQuantityDiscount)
    {
        this(validateProductQuantityDiscount.getRuleId(), validateProductQuantityDiscount.getProductId(), validateProductQuantityDiscount.getProductQuantity(), validateProductQuantityDiscount.isCantbemore());
    }
}
