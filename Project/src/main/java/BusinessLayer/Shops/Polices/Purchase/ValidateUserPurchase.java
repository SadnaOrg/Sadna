package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

public class ValidateUserPurchase implements PurchasePolicy{

    @Override
    public boolean isValid(User u, Basket basket) {
        return false;
    }
}
