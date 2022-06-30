package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

public interface ValidatePurchasePolicy extends PurchasePolicy{

    public boolean isValid(User u, Basket basket);
}