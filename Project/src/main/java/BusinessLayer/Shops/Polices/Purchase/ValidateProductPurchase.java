package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

public class ValidateProductPurchase implements ValidatePurchasePolicy{

    private final int policyLogicId;
    int productId;
    int productQuantity;
    //if true then can't be higher than false can't be lower than
    boolean cantbemore;

    public ValidateProductPurchase(int productId, int productQuantity, boolean cantbemore) {
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.cantbemore = cantbemore;
        this.policyLogicId = purchaseLogicId.incrementAndGet();
    }

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
    @Override
    public LogicPurchasePolicy getLogicRule(int searchConnectId)
    {
        return null;
    }

    @Override
    public int getID() {
        return this.policyLogicId;
    }
}
