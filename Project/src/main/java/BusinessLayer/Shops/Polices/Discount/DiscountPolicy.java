package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public abstract class DiscountPolicy implements DiscountPolicyInterface{

    protected DiscountPolicyInterface discountPolicy;
    int discountId;

    public DiscountPolicy(DiscountPolicyInterface discountPolicy)
    {
        this.discountPolicy = discountPolicy;
        this.discountId = discountPolicy.getDiscountId()+1;
    }

    public double calculateDiscount(Basket basket){
        return discountPolicy.calculateDiscount(basket);
    }

    public int getDiscountId() {
        return discountId;
    }

    public DiscountPolicyInterface getDiscountPolicy() {
        return discountPolicy;
    }

}
