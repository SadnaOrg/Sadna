package ServiceLayer.Objects.Policies.Discount;

public record ValidateBasketQuantityDiscount(int ruleId,int basketquantity,boolean cantBeMore) implements DiscountPred{

    public ValidateBasketQuantityDiscount(BusinessLayer.Shops.Polices.Discount.ValidateBasketQuantityDiscount discountPred) {

        this(discountPred.getRuleId(),discountPred.getBasketquantity(),discountPred.isCantBeMore());
    }
}
