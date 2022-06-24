package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class DiscountMaxPolicy implements NumericDiscountRules{
    private int connectId;

    private Collection<DiscountRules> discountPolicies;

    public DiscountMaxPolicy(Collection<DiscountRules> discountPolicies) {
        this.discountPolicies= new ArrayList<>();
        this.discountPolicies.addAll(discountPolicies);
        this.connectId = atomicconnectId.incrementAndGet();
    }

    public DiscountMaxPolicy(DiscountRules discountPolicy) {
        this.discountPolicies= new ArrayList<>();
        if(discountPolicy != null) {
            this.discountPolicies.add(discountPolicy);
        }
        this.connectId = atomicconnectId.incrementAndGet();
    }

    public DiscountMaxPolicy(int connectId, Collection<DiscountRules> discountPolicies) {
        this.connectId = connectId;
        this.discountPolicies = discountPolicies;
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

    public int getConnectId() {
        return connectId;
    }

    public Collection<DiscountRules> getDiscountPolicies() {
        return discountPolicies;
    }

    @Override
    public boolean removeSonDiscount(int ID) {
        for (DiscountRules discountRule :discountPolicies) {
            if (discountRule.getID() ==ID) {
                return remove(discountRule);
            }
            boolean temp = false ;
            if(discountRule instanceof NumericDiscountRules)
                temp = ((NumericDiscountRules) discountRule).removeSonDiscount(ID);
            if(discountRule instanceof LogicDiscountRules)
                temp = ((LogicDiscountRules) discountRule).removeSonDiscount(ID);
            if(temp)
            {
                return temp;
            }
        }
        return false;
    }

    @Override
    public boolean removeSonPredicate(int ID)
    {
        for (DiscountRules discountRule :discountPolicies) {
            boolean temp = false ;
            if(discountRule instanceof LogicDiscountRules ||discountRule instanceof NumericDiscountRules)
                temp =  discountRule.removeSonPredicate(ID);
            if(temp)
            {
                return true;
            }
        }
        return false;
    }

}
