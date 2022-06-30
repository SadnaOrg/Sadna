package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Mappers.Converter;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

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
    public boolean remove(int purchasePolicy) {
        throw new IllegalStateException("can't remove her");
    }

    @Override
    public int getID(){
        return this.policyLogicId;
    }
    @Override
    public boolean removeChild(int policy){

        if(purchasePolicyAllow instanceof LogicPurchasePolicy)
        {
            return ((LogicPurchasePolicy) purchasePolicyAllow).removeChild(policy);
        }
        if(validatePurchase instanceof LogicPurchasePolicy)
        {
            return ((LogicPurchasePolicy) validatePurchase).removeChild(policy);
        }
        return false;
    }


    public int getPolicyLogicId() {
        return policyLogicId;
    }

    public PurchasePolicy getPurchasePolicyAllow() {
        return purchasePolicyAllow;
    }

    public PurchasePolicy getValidatePurchase() {
        return validatePurchase;
    }

    @Override
    public ORM.Shops.Purchases.PurchasePolicy toEntity(Converter c,ORM.Shops.Shop shop) {
        return c.toEntity(this, shop);
    }
}