package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.Collection;
import java.util.Iterator;

public class DiscountMaxPolicy implements NumericDiscountRules{

    Collection<DiscountRules> discountPolicies;

    public DiscountMaxPolicy(Collection<DiscountRules> discountPolicies) {
        this.discountPolicies = discountPolicies;
    }

    @Override
    public double calculateDiscount(Basket basket) {
        Iterator<DiscountRules> i = discountPolicies.iterator();
        double maxprice =i.next().calculateDiscount(basket);
        while (i.hasNext())
        {
            maxprice= Math.max(maxprice,i.next().calculateDiscount(basket));
        }
        return maxprice;
    }
}
