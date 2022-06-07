package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.concurrent.atomic.AtomicInteger;

public interface DiscountPred{
    AtomicInteger atomicRuleID= new AtomicInteger(0);
    public boolean validateDiscount(Basket basket);
}
