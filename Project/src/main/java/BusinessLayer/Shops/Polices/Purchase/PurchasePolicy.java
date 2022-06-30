package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

import java.util.concurrent.atomic.AtomicInteger;

public interface PurchasePolicy extends ConvertablePurchase{
    AtomicInteger purchaseLogicId = new AtomicInteger(0);
    public boolean isValid(User u, Basket basket);
    public LogicPurchasePolicy getLogicRule(int searchConnectId);
    public int getID();

}