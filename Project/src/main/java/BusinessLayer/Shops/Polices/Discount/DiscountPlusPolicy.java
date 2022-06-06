package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.Collection;
import java.util.Iterator;

public class DiscountPlusPolicy implements NumericDiscountRules{

    Collection<DiscountRules> discountPolicies;

    public DiscountPlusPolicy(Collection<DiscountRules> discountPolicies) {
        this.discountPolicies = discountPolicies;
    }

    @Override
    public double calculateDiscount(Basket basket) {
        Iterator<DiscountRules> i = discountPolicies.iterator();
        double price =i.next().calculateDiscount(basket);
        while (i.hasNext())
        {
            price += i.next().calculateDiscount(basket);
        }
        return price;
    }
}
