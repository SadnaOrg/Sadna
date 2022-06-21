package ServiceLayer.Objects.Policies.Purchase;

import java.util.ArrayList;
import java.util.Collection;

public record PurchaseOrPolicy(Collection<PurchasePolicy> purchasePolicies, int policyLogicId) implements PurchasePolicy{
    public PurchaseOrPolicy(BusinessLayer.Shops.Polices.Purchase.PurchaseOrPolicy policy)
    {
        this(policy.getPolicyLogicId());
        for(BusinessLayer.Shops.Polices.Purchase.PurchasePolicy buispolicy: policy.getPurchasePolicies())
        {
            purchasePolicies.add(PurchasePolicy.makeServicePurchasePolicy(buispolicy));
        }
    }
    public PurchaseOrPolicy(int id)
    {
        this(new ArrayList<>(),id);
    }
}
