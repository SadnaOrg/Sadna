package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class DiscountMaxPolicy implements NumericDiscountRules{
    private int connectId;

    Collection<DiscountRules> discountPolicies;

    public DiscountMaxPolicy(Collection<DiscountRules> discountPolicies) {
        this.discountPolicies= new ArrayList<>();
        this.discountPolicies.addAll(discountPolicies);
        this.connectId = atomicconnectId.incrementAndGet();
    }

    public DiscountMaxPolicy(DiscountRules discountPolicy) {
        this.discountPolicies= new ArrayList<>();
        this.discountPolicies.add(discountPolicy);
        this.connectId = atomicconnectId.incrementAndGet();
    }

    @Override
    public double calculateDiscount(Basket basket) {
        Iterator<DiscountRules> i = discountPolicies.iterator();
        double maxprice = 0;
        while (i.hasNext()) {
            maxprice = Math.max(maxprice, i.next().calculateDiscount(basket));
        }
        return maxprice;
    }


    @Override
    public void add(DiscountRules discountRules) {
        discountPolicies.add(discountRules);
    }

    @Override
    public boolean remove(DiscountRules discountRules) {
        return discountPolicies.remove(discountRules);
    }

    public NumericDiscountRules getNumericRule(int searchConnectId) {
        if (this.connectId == searchConnectId)
            return this;
        else {
            for (DiscountRules discountRules : discountPolicies) {
                NumericDiscountRules findrule = discountRules.getNumericRule(searchConnectId);
                if (findrule != null)
                    return findrule;
            }
        }
        return null;
    }


    public LogicDiscountRules getLogicRule(int searchConnectId)
    {
        for (DiscountRules discountRules : discountPolicies) {
            LogicDiscountRules findrule = discountRules.getLogicRule(searchConnectId);
            if (findrule != null)
                return findrule;
        }
        return null;
    }

    @Override
    public int getID(){
        return this.connectId;
    }
}
