package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.Collection;

public class DiscountOrPolicy implements LogicDiscountRules{

    private Collection<DiscountPred> discountPreds;

    public DiscountOrPolicy(DiscountPolicy discountPolicy, Collection<DiscountPred> discountPreds) {
        this.discountPreds = discountPreds;
    }

    @Override
    public boolean validateDiscount(Basket basket){
        for(DiscountPred discountPred: discountPreds)
        {
            if(discountPred.validateDiscount(basket))
                return true;
        }
        return false;
    }
}
