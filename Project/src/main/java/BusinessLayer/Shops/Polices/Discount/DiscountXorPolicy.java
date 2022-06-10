package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.ArrayList;
import java.util.Collection;

public class DiscountXorPolicy implements LogicDiscountRules{
    private int connectId;

    private DiscountRules discountRules1;
    private DiscountRules discountRules2;
    //if all true discountRules1 else discountRules2;
    private Collection<DiscountPred> tieBreakers;

    public DiscountXorPolicy(DiscountRules discountRules1, DiscountRules discountRules2,  Collection<DiscountPred> tieBreakers) {
        this.discountRules1 = discountRules1;
        this.discountRules2 = discountRules2;
        this.tieBreakers = new ArrayList<>();
        this.tieBreakers.addAll(tieBreakers);
        this.connectId = atomicconnectId.incrementAndGet();
    }
    public DiscountXorPolicy(DiscountRules discountRules1, DiscountRules discountRules2,  DiscountPred tieBreaker) {
        this.discountRules1 = discountRules1;
        this.discountRules2 = discountRules2;
        this.tieBreakers = new ArrayList<>();
        this.tieBreakers.add(tieBreaker);
        this.connectId = atomicconnectId.incrementAndGet();
    }
    @Override
    public double calculateDiscount(Basket basket){
        double dr1=discountRules1.calculateDiscount(basket);
        double dr2=discountRules2.calculateDiscount(basket);
        if(dr1>0)
        {
            if (dr2>0)
            {
                for (DiscountPred tieBreaker: tieBreakers) {
                    if (!tieBreaker.validateDiscount(basket))
                        return dr2;
                }
            }
            return dr1;
        }
        if (dr2>0)
            return dr2;

        return 0;
    }
    public NumericDiscountRules getNumericRule(int searchConnectId) {
        NumericDiscountRules findrule = discountRules1.getNumericRule(searchConnectId);
        if (findrule==null)
        {
            findrule = discountRules2.getNumericRule(searchConnectId);
        }
        return findrule;
    }

    public LogicDiscountRules getLogicRule(int searchConnectId)
    {
        if(this.connectId == searchConnectId)
            return this;
        LogicDiscountRules findrule = discountRules1.getLogicRule(searchConnectId);
        if (findrule==null)
        {
            findrule = discountRules2.getLogicRule(searchConnectId);
        }
        return findrule;
    }

    @Override
    public void add(DiscountPred discountPred) {
        tieBreakers.add(discountPred);
    }
    @Override
    public boolean remove(DiscountPred discountPred) {
        return tieBreakers.remove(discountPred);
    }

}
