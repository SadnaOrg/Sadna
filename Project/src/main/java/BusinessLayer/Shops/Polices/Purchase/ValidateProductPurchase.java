package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

public class ValidateProductPurchase implements ValidatePurchasePolicy{

    int productId;
    int productQuantity;
    //if true then can't be higher than false can't be lower than
    boolean cantbemore;

    public boolean isValid(User u, Basket basket) {
        if (basket.getProducts().containsKey(productId))
        {
            if(cantbemore)
                return basket.getProducts().get(productId)<=productQuantity;
            else
                return basket.getProducts().get(productId)>=productQuantity;
        }
        return true;
    }

}
