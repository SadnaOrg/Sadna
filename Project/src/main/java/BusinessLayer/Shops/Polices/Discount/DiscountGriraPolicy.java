package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class DiscountGriraPolicy implements LogicDiscountRules{
    private int connectId;

    private DiscountPred discountPred;
    private DiscountRules discountPolicy;

    public DiscountGriraPolicy(DiscountPred discountPred, DiscountPolicy discountPolicy) {
        this.discountPred = discountPred;
        this.discountPolicy = discountPolicy;
        this.connectId = atomicconnectId.incrementAndGet();
    }

    @Override
    public double calculateDiscount(Basket basket) {
        if(discountPred.validateDiscount(basket))
            return discountPolicy.calculateDiscount(basket);
        return 0;

    }
}
