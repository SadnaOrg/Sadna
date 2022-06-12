package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Shops.Polices.Discount.LogicDiscountRules;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

import java.util.ArrayList;
import java.util.Collection;

public class PurchaseAndPolicy implements LogicPurchasePolicy{

    private Collection<PurchasePolicy> purchasePolicies;
    private int policyLogicId;

    public PurchaseAndPolicy() {
        purchasePolicies = new ArrayList<>();
    }

    public PurchaseAndPolicy(Collection<PurchasePolicy> purchasePolicies) {
        this.purchasePolicies = new ArrayList<>();
        this.purchasePolicies.addAll(purchasePolicies);
        this.policyLogicId = purchaseLogicId.incrementAndGet();
    }
    public PurchaseAndPolicy(PurchasePolicy purchasePolicy) {
        this.purchasePolicies = new ArrayList<>();
        this.purchasePolicies.add(purchasePolicy);
        this.policyLogicId = purchaseLogicId.incrementAndGet();
    }
    @Override
    public boolean isValid(User u, Basket basket) {
        for (PurchasePolicy purchasePolicy: purchasePolicies)
        {
            if(!purchasePolicy.isValid(u,basket))
                return false;
        }
        return true;
    }

    public LogicPurchasePolicy getLogicRule(int searchConnectId)
    {
        if(this.policyLogicId == searchConnectId)
            return this;
        for (PurchasePolicy policy : purchasePolicies)
        {
            LogicPurchasePolicy findrule = policy.getLogicRule(searchConnectId);
            if (findrule!=null)
                return findrule;
        }
        return null;
    }

    public void add(PurchasePolicy purchasePolicy)
    {
        this.purchasePolicies.add(purchasePolicy);
    }

    public boolean remove(PurchasePolicy purchasePolicy){
        return this.purchasePolicies.remove(purchasePolicy);
    }

    @Override
    public int getID(){
        return this.policyLogicId;
    }
}
