package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class DiscountGriraPolicy implements NumericDiscountRules{

    private DiscountPred discountPred;
    private DiscountPolicy discountPolicy;

    @Override
    public double calculateDiscount(Basket basket) {
        if(discountPred.validateDiscount(basket))
            return discountPolicy.calculateDiscount(basket);
        return 0;

    }
}
