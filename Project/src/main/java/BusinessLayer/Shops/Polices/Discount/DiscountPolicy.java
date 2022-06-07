package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.concurrent.atomic.AtomicInteger;

public interface DiscountPolicy extends DiscountRules{
    AtomicInteger atomicDiscountID= new AtomicInteger(0);
    public double calculateDiscount(Basket basket);

}
