package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class DiscountGriraPolicy implements DiscountRules{

    private DiscountPred discountPred;
    private DiscountPolicy discountPolicy;

    public DiscountGriraPolicy(DiscountPred discountPred, DiscountPolicy discountPolicy) {
        this.discountPred = discountPred;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public double calculateDiscount(Basket basket) {
        if(discountPred.validateDiscount(basket))
            return discountPolicy.calculateDiscount(basket);
        return 0;

    }
}
