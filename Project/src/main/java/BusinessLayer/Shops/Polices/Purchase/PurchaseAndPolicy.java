package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

import java.util.ArrayList;
import java.util.Collection;

public class PurchaseAndPolicy implements LogicPurchasePolicy{

    private Collection<PurchasePolicy> purchasePolicies;

    public PurchaseAndPolicy() {
    }

    public PurchaseAndPolicy(Collection<PurchasePolicy> purchasePolicies) {
        this.purchasePolicies = new ArrayList<>();
        this.purchasePolicies.addAll(purchasePolicies);
    }
    public PurchaseAndPolicy(PurchasePolicy purchasePolicy) {
        this.purchasePolicies = new ArrayList<>();
        this.purchasePolicies.add(purchasePolicy);
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

    public void add(PurchasePolicy purchasePolicy)
    {
        this.purchasePolicies.add(purchasePolicy);
    }

    public boolean remove(PurchasePolicy purchasePolicy){
        return this.purchasePolicies.remove(purchasePolicy);
    }
}
