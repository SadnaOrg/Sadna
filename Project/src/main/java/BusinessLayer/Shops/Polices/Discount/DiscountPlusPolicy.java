package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class DiscountPlusPolicy implements NumericDiscountRules{
    private int connectId;
    private Collection<DiscountRules> discountPolicies;

    public DiscountPlusPolicy() {
        this.connectId = atomicconnectId.incrementAndGet();
        this.discountPolicies = new ArrayList<>();
    }

    public DiscountPlusPolicy(Collection<DiscountRules> discountPolicies) {
        this.discountPolicies = new ArrayList<>();
        this.discountPolicies.addAll(discountPolicies);
        this.connectId = atomicconnectId.incrementAndGet();
    }

    public DiscountPlusPolicy(DiscountRules discountPolicy) {
        this.discountPolicies = new ArrayList<>();
        this.discountPolicies.add(discountPolicy);
        this.connectId = atomicconnectId.incrementAndGet();
    }

    public DiscountPlusPolicy(int connectId, Collection<DiscountRules> discountPolicies) {
        this.connectId = connectId;
        this.discountPolicies = discountPolicies;
    }

    @Override
    public double calculateDiscount(Basket basket) {
        Iterator<DiscountRules> i = discountPolicies.iterator();
        double price =0;
        while (i.hasNext())
        {
            price += i.next().calculateDiscount(basket);
        }
        return price;
    }

    @Override
    public void add(DiscountRules discountRules) {
        discountPolicies.add(discountRules);
    }

    @Override
    public boolean remove(DiscountRules discountRules) {
        return discountPolicies.remove(discountRules);
    }

    public NumericDiscountRules getNumericRule(int searchConnectId)
    {
        if(this.connectId==searchConnectId)
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

    public int getConnectId() {
        return connectId;
    }

    public Collection<DiscountRules> getDiscountPolicies() {
        return discountPolicies;
    }
}
