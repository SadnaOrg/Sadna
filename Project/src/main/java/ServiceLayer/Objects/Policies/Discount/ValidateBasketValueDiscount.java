package ServiceLayer.Objects.Policies.Discount;



public record ValidateBasketValueDiscount(int ruleId, double basketvalue , boolean cantBeMore) implements DiscountPred{

    public ValidateBasketValueDiscount(BusinessLayer.Shops.Polices.Discount.ValidateBasketValueDiscount validateBasketValueDiscount)
    {
        this(validateBasketValueDiscount.getRuleId(), validateBasketValueDiscount.getBasketvalue(), validateBasketValueDiscount.isCantBeMore());
    }
}
