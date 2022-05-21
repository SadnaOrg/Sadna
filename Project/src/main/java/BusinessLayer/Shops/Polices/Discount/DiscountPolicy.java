package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public abstract class DiscountPolicy implements DiscountPolicyInterface{

    protected DiscountPolicyInterface discountPolicy;

    public DiscountPolicy(DiscountPolicyInterface discountPolicy)
    {
        this.discountPolicy = discountPolicy;
    }

    public double calculateDiscount(Basket basket){
        return discountPolicy.calculateDiscount(basket);
    }


}
