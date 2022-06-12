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
        this.discountPreds.add(discountPred);
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

    public Collection<DiscountPred> getDiscountPreds() {
        return discountPreds;
    }

    public DiscountRules getDiscountPolicy() {
        return discountPolicy;
    }

    public int getConnectId() {
        return connectId;
    }
}
