package ServiceLayer.Objects.Policies.Discount;

public record ShopDiscount(int discountId, int basketQuantity,double discount)implements DiscountRules {

    public ShopDiscount(BusinessLayer.Shops.Polices.Discount.ShopDiscount shopDiscount)
    {
        this(shopDiscount.getDiscountId(), shopDiscount.getBasketQuantity(), shopDiscount.getDiscount());
    }
}
