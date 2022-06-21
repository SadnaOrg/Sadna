package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Mappers.ShopMappers.ConvertablePolicy;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

import java.util.concurrent.atomic.AtomicInteger;

public interface PurchasePolicy extends ConvertablePolicy {
    AtomicInteger purchaseLogicId = new AtomicInteger(0);
    public boolean isValid(User u, Basket basket);
    public LogicPurchasePolicy getLogicRule(int searchConnectId);
    public int getID();

}
