package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Shops.Polices.Discount.DiscountPred;
import BusinessLayer.Shops.Polices.Discount.DiscountRules;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public interface LogicPurchasePolicy extends PurchasePolicy{

    public boolean isValid(User u, Basket basket);
    public void add(PurchasePolicy purchasePolicy);
    public boolean remove(int purchasePolicy);
    public boolean removeChild(int policy);
}