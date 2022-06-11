package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.ArrayList;
import java.util.Collection;

public class DiscountAndPolicy implements LogicDiscountRules{
     private Collection<DiscountPred> discountPreds;
     private DiscountRules discountPolicy;
     private int connectId;

    public DiscountAndPolicy(Collection<DiscountPred> discountPreds,DiscountRules discountPolicy) {
        this.discountPreds = new ArrayList<>();
        this.discountPreds.addAll(discountPreds);
        this.discountPolicy = discountPolicy;
        this.connectId = atomicconnectId.incrementAndGet();
    }


    public DiscountAndPolicy(DiscountPred discountPred,DiscountRules discountPolicy) {
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

    @Override
    public void add(DiscountPred discountPred) {
        discountPreds.add(discountPred);
    }
    @Override
    public boolean remove(DiscountPred discountPred) {
        return discountPreds.remove(discountPred);
    }

    public NumericDiscountRules getNumericRule(int searchConnectId) {
        return discountPolicy.getNumericRule(searchConnectId);
    }


    public LogicDiscountRules getLogicRule(int searchConnectId)
    {
        if(this.connectId == searchConnectId)
            return this;
        return discountPolicy.getLogicRule(searchConnectId);
    }

    @Override
    public int getID(){
        return this.connectId;
    }
}
