package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

public interface PurchasePolicy {

    public boolean isValid(User u, Basket basket);
    public LogicPurchasePolicy getLogicRule(int searchConnectId);

}
