package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

import java.util.ArrayList;
import java.util.Collection;

public class PurchaseOrPolicy implements LogicPurchasePolicy{

    private Collection<PurchasePolicy> purchasePolicies;
    private int policyLogicId;

    public PurchaseOrPolicy(Collection<PurchasePolicy> purchasePolicies, int policyLogicId) {
        this.purchasePolicies = purchasePolicies;
        this.policyLogicId = policyLogicId;
    }

    public PurchaseOrPolicy(Collection<PurchasePolicy> purchasePolicies) {
        this.purchasePolicies = new ArrayList<>();
        this.purchasePolicies.addAll(purchasePolicies);
        this.policyLogicId = purchaseLogicId.incrementAndGet();
    }

    public PurchaseOrPolicy(PurchasePolicy purchasePolicy) {
        this.purchasePolicies = new ArrayList<>();
        if(purchasePolicy != null)
            this.purchasePolicies.add(purchasePolicy);
        this.policyLogicId = purchaseLogicId.incrementAndGet();
    }

    @Override
    public boolean isValid(User u, Basket basket) {
        for (PurchasePolicy purchasePolicy: purchasePolicies)
        {
            if(purchasePolicy.isValid(u,basket))
                return true;
        }
        return false;
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

    public boolean remove(int purchasePolicy){
        for (PurchasePolicy p:
                purchasePolicies) {
            if(p.getID() == purchasePolicy){
                purchasePolicies.remove(p);
                return true;
            }
        }
        return false;
    }

    @Override
    public int getID(){
        return this.policyLogicId;
    }

    @Override
    public boolean removeChild(int policy){
        for (PurchasePolicy purchasePolicy: purchasePolicies)
        {
            if(purchasePolicy.getID() == policy)
            {
                return remove(policy);
            }
            if(purchasePolicy instanceof LogicPurchasePolicy)
            {
                return ((LogicPurchasePolicy) purchasePolicy).removeChild(policy);
            }
        }
        return false;
    }

    public Collection<PurchasePolicy> getPurchasePolicies() {
        return purchasePolicies;
    }

    public int getPolicyLogicId() {
        return policyLogicId;
    }
}
