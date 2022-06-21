package ServiceLayer.Objects.Policies.Discount;

public record RelatedGroupDiscount(int discountId,String category, double discount) implements DiscountRules{

    public RelatedGroupDiscount(BusinessLayer.Shops.Polices.Discount.RelatedGroupDiscount relatedGroupDiscount)
    {
        this(relatedGroupDiscount.getDiscountId(), relatedGroupDiscount.getCategory(), relatedGroupDiscount.getDiscount());
    }

}
