package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.ArrayList;
import java.util.Collection;

public class DiscountAndPolicy implements LogicDiscountRules{
    private int connectId;
     private Collection<DiscountPred> discountPreds;
     private DiscountRules discountPolicy;

    public DiscountAndPolicy(Collection<DiscountPred> discountPreds,DiscountRules discountPolicy) {
        this.discountPreds = new ArrayList<>();
        this.discountPreds.addAll(discountPreds);
        this.discountPolicy = discountPolicy;
        this.connectId = atomicconnectId.incrementAndGet();
    }


    public DiscountAndPolicy(DiscountPred discountPred,DiscountRules discountPolicy) {
        this.discountPreds = new ArrayList<>();
        if(discountPred != null) {
            this.discountPreds.add(discountPred);
        }
        this.connectId = atomicconnectId.incrementAndGet();
        this.discountPolicy = discountPolicy;

    }

    public DiscountAndPolicy(int connectId,Collection<DiscountPred> discountPreds, DiscountRules discountPolicy) {
        this.discountPreds = discountPreds;
        this.discountPolicy = discountPolicy;
        this.connectId = connectId;
    }

    @Override
    public double calculateDiscount(Basket basket){
        if(!validate())
            return 0;
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
        if(validate())
            return discountPolicy.getNumericRule(searchConnectId);
        return null;
    }


    public LogicDiscountRules getLogicRule(int searchConnectId)
    {
        if(this.connectId == searchConnectId)
            return this;
        if(!validate())
            return null;
        return discountPolicy.getLogicRule(searchConnectId);
    }

    @Override
    public int getID(){
        return this.connectId;
    }


    public Collection<DiscountPred> getDiscountPreds() {
        return discountPreds;
    }

    public DiscountRules getDiscountPolicy() {
        return discountPolicy;
    }

    public int getConnectId() {
        return connectId;
    }

    @Override
    public boolean removeSonPredicate(DiscountPred discountPred) {
        for (DiscountPred discountPred1 :discountPreds) {
            if (discountPred1.getID() == discountPred.getID()) {
                return remove(discountPred);
            }
        }
        boolean temp = false ;
        if(discountPolicy instanceof NumericDiscountRules ||discountPolicy instanceof LogicDiscountRules)
            temp = (discountPolicy.removeSonPredicate(discountPred));
        return temp;
    }

    @Override
    public boolean removeSonDiscount(DiscountRules removeFromConnectId) {
        if (discountPolicy instanceof NumericDiscountRules)
            return ((NumericDiscountRules) discountPolicy).removeSonDiscount(removeFromConnectId);
        if (discountPolicy instanceof LogicDiscountRules)
            return ((LogicDiscountRules) discountPolicy).removeSonDiscount(removeFromConnectId);
        return false;
    }

    @Override
    public void setPolicy(DiscountRules discountRules) {
        this.discountPolicy = discountRules;
    }

    @Override
    public boolean validate() {
        return (discountPolicy != null);
    }
}
