package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.ArrayList;
import java.util.Collection;

public class DiscountOrPolicy implements LogicDiscountRules{
    private int connectId;
    private DiscountPolicy discountPolicy;
    private int ruleId;
    private Collection<DiscountPred> discountPreds;

    public DiscountOrPolicy(Collection<DiscountPred> discountPreds,DiscountPolicy discountPolicy) {
        this.discountPreds = new ArrayList<>();
        this.discountPreds.addAll(discountPreds);
        this.connectId = atomicconnectId.incrementAndGet();
        this.discountPolicy = discountPolicy;
    }

    public DiscountOrPolicy(DiscountPred discountPred,DiscountPolicy discountPolicy) {
        this.discountPreds = new ArrayList<>();
        this.discountPreds.add(discountPred);
        this.connectId = atomicconnectId.incrementAndGet();
        this.discountPolicy = discountPolicy;
    }

    @Override
    public double calculateDiscount(Basket basket){
        for(DiscountPred discountPred: discountPreds)
        {
            if(discountPred.validateDiscount(basket))
                return discountPolicy.calculateDiscount(basket);
        }
        return 0;
    }


    public void add(DiscountPred discountPred) {
        discountPreds.add(discountPred);
    }

    public boolean remove(DiscountPred discountPred) {
        return discountPreds.remove(discountPred);
    }

}
