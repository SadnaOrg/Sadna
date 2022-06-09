package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.concurrent.atomic.AtomicInteger;

public interface LogicDiscountRules extends DiscountRules {

    public double calculateDiscount(Basket basket);
    public void add(DiscountPred discountRules);
    public boolean remove(DiscountPred discountRules);
}
