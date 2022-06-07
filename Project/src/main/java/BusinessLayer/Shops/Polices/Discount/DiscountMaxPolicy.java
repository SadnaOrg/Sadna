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
        double maxprice = i.next().calculateDiscount(basket);
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
}
