package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Mappers.ShopMappers.Converter;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;
import com.SadnaORM.ShopImpl.ShopObjects.Policies.PurchasePolicyDTO;

public class ValidateProductPurchase implements ValidatePurchasePolicy{

    private final int policyLogicId;
    private int productId;
    private int productQuantity;
    //if true then can't be higher than false can't be lower than
    private boolean cantbemore;

    public ValidateProductPurchase(int productId, int productQuantity, boolean cantbemore) {
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.cantbemore = cantbemore;
        this.policyLogicId = purchaseLogicId.incrementAndGet();
    }

    public ValidateProductPurchase(int policyLogicId, int productId, int productQuantity, boolean cantbemore) {
        this.policyLogicId = policyLogicId;
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.cantbemore = cantbemore;
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

    public int getPolicyLogicId() {
        return policyLogicId;
    }

    public int getProductId() {
        return productId;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public boolean isCantbemore() {
        return cantbemore;
    }

    @Override
    public PurchasePolicyDTO conversion(Converter c) {
        return c.convert(this);
    }
}
