package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.concurrent.atomic.AtomicInteger;

public interface DiscountRules {
    AtomicInteger atomicconnectId= new AtomicInteger(0);
    public double calculateDiscount(Basket basket);
    public NumericDiscountRules getNumericRule(int searchConnectId);
    public LogicDiscountRules getLogicRule(int searchConnectId);
    public int getID();
    public boolean removeSonPredicate(int ID);

}
