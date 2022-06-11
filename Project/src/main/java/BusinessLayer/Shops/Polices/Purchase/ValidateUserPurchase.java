package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

public class ValidateUserPurchase implements ValidatePurchasePolicy{

    @Override
    public boolean isValid(User u, Basket basket) {
        return false;
    }
    @Override
    public LogicPurchasePolicy getLogicRule(int searchConnectId)
    {
        return null;
    }
}
