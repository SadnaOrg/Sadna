package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public abstract class DiscountPolicy {

    protected DiscountPolicy discountPolicy;

    public DiscountPolicy()
    {
        this.discountPolicy = this;
    }

    abstract double calculateDiscount(Basket basket);


}
