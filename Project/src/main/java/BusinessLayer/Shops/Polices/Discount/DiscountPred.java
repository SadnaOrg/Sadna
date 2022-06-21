package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Mappers.ShopMappers.ConvertablePred;
import BusinessLayer.Users.Basket;

import java.util.concurrent.atomic.AtomicInteger;

public interface DiscountPred extends ConvertablePred {
    AtomicInteger atomicRuleID= new AtomicInteger(0);
    public boolean validateDiscount(Basket basket);
    public int getID();
}
