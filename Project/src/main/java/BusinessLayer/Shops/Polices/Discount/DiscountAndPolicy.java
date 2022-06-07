package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.ArrayList;
import java.util.Collection;

public class DiscountAndPolicy implements LogicDiscountRules{
     private Collection<DiscountPred> discountPreds;
     private DiscountPolicy discountPolicy;
     private int connectId;

    public DiscountAndPolicy(Collection<DiscountPred> discountPreds,DiscountPolicy discountPolicy) {
        this.discountPreds = new ArrayList<>();
        this.discountPreds.addAll(discountPreds);
        this.discountPolicy = discountPolicy;
        this.connectId = atomicconnectId.incrementAndGet();
    }

    public DiscountAndPolicy(DiscountPolicy discountPolicy) {
    }

    public DiscountAndPolicy(DiscountPred discountPred,DiscountPolicy discountPolicy) {
        this.discountPreds = new ArrayList<>();
        this.discountPreds.add(discountPred);
        this.connectId = atomicconnectId.incrementAndGet();
        this.discountPolicy = discountPolicy;

    }


    @Override
    public double calculateDiscount(Basket basket){
         for(DiscountPred discountPred: discountPreds)
         {
             if(!discountPred.validateDiscount(basket))
                 return 0;
         }
         return discountPolicy.calculateDiscount(basket);
     }


    public void add(DiscountPred discountPred) {
        discountPreds.add(discountPred);
    }

    public boolean remove(DiscountPred discountPred) {
        return discountPreds.remove(discountPred);
    }


}
