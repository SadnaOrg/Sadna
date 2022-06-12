package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

import java.util.Collection;

public class PurchaseGriraPolicy implements LogicPurchasePolicy{

    private int policyLogicId;

    private PurchasePolicy purchasePolicyAllow;
    private PurchasePolicy validatePurchase;

    public PurchaseGriraPolicy(PurchasePolicy purchasePolicyAllow, PurchasePolicy validatePurchase) {
        this.purchasePolicyAllow = purchasePolicyAllow;
        this.validatePurchase = validatePurchase;
        this.policyLogicId = purchaseLogicId.incrementAndGet();
    }

    @Override
    public boolean isValid(User u, Basket basket) {
        if(validatePurchase.isValid(u,basket))
        {
            return purchasePolicyAllow.isValid(u,basket);
        }

        return false;
    }
    public LogicPurchasePolicy getLogicRule(int searchConnectId)
    {
        LogicPurchasePolicy findrule = purchasePolicyAllow.getLogicRule(searchConnectId);
        if (findrule==null)
            findrule = validatePurchase.getLogicRule(searchConnectId);
        return findrule;
    }

    @Override
    public void add(PurchasePolicy purchasePolicy) {
        throw new IllegalStateException("can't add her");
    }

    @Override
    public boolean remove(PurchasePolicy purchasePolicy) {
        throw new IllegalStateException("can't remove her");
    }

    @Override
    public int getID(){
        return this.policyLogicId;
    }
}
