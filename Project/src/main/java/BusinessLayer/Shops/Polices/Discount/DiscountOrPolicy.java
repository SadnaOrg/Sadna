package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.Collection;

public class DiscountOrPolicy implements LogicDiscountRules{

    private DiscountPolicy discountPolicy;
    private Collection<DiscountPred> discountPreds;

    public DiscountOrPolicy(DiscountPolicy discountPolicy, Collection<DiscountPred> discountPreds) {
        this.discountPolicy = discountPolicy;
        this.discountPreds = discountPreds;
    }

    @Override
    public double calculateDiscount(Basket basket) {
        for(DiscountPred discountPred: discountPreds)
        {
            if(discountPred.validateDiscount(basket))
                return discountPolicy.calculateDiscount(basket);
        }
        return 0;
    }
}
