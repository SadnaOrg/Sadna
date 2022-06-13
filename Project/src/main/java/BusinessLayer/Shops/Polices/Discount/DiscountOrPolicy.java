package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.ArrayList;
import java.util.Collection;

public class DiscountOrPolicy implements LogicDiscountRules{
    private int connectId;
    private DiscountRules discountPolicy;
    private Collection<DiscountPred> discountPreds;

    public DiscountOrPolicy(Collection<DiscountPred> discountPreds,DiscountRules discountPolicy) {
        this.discountPreds = new ArrayList<>();
        this.discountPreds.addAll(discountPreds);
        this.connectId = atomicconnectId.incrementAndGet();
        this.discountPolicy = discountPolicy;
    }

    public DiscountOrPolicy(DiscountPred discountPred,DiscountRules discountPolicy) {
        this.discountPreds = new ArrayList<>();
        if(discountPolicy != null) {
            this.discountPreds.add(discountPred);
        }
        this.connectId = atomicconnectId.incrementAndGet();
        this.discountPolicy = discountPolicy;
    }

    public DiscountOrPolicy(int connectId, DiscountRules discountPolicy, Collection<DiscountPred> discountPreds) {
        this.connectId = connectId;
        this.discountPolicy = discountPolicy;
        this.discountPreds = discountPreds;
    }

    @Override
    public double calculateDiscount(Basket basket){
        if(!validate())
            return 0;
        for(DiscountPred discountPred: discountPreds)
        {
            if(discountPred.validateDiscount(basket))
                return discountPolicy.calculateDiscount(basket);
        }
        return 0;
    }

    @Override
    public void add(DiscountPred discountPred) {
        discountPreds.add(discountPred);
    }
    @Override
    public boolean remove(int ID) {
        for (DiscountPred pred:
                discountPreds) {
            if(pred.getID() == ID){
                discountPreds.remove(pred);
                return true;
            }
        }
        return false;
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
    public boolean removeSonDiscount(int ID) {
        if (discountPolicy instanceof NumericDiscountRules)
            return ((NumericDiscountRules) discountPolicy).removeSonDiscount(ID);
        if (discountPolicy instanceof LogicDiscountRules)
            return ((LogicDiscountRules) discountPolicy).removeSonDiscount(ID);
        return false;
    }

    @Override
    public void setPolicy(DiscountRules discountRules) {
        this.discountPolicy = discountRules;
    }

    @Override
    public boolean validate() {
        return this.discountPolicy != null;
    }

    @Override
    public boolean removeSonPredicate(int ID) {
        for (DiscountPred discountPred1 :discountPreds) {
            if (discountPred1.getID() == ID) {
                return remove(ID);
            }
        }
        boolean temp = false ;
        if(discountPolicy instanceof NumericDiscountRules ||discountPolicy instanceof LogicDiscountRules)
            temp = (discountPolicy.removeSonPredicate(ID));
        return temp;
    }
}
