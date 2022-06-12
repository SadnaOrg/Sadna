package ServiceLayer.Objects.Policies.Purchase;

import java.util.ArrayList;
import java.util.Collection;

public record PurchaseAndPolicy(Collection<PurchasePolicy> purchasePolicies, int policyLogicId) implements PurchasePolicy{
    public PurchaseAndPolicy(BusinessLayer.Shops.Polices.Purchase.PurchaseAndPolicy policy)
    {
        this(policy.getPolicyLogicId());
        for(BusinessLayer.Shops.Polices.Purchase.PurchasePolicy buispolicy: policy.getPurchasePolicies())
        {
            purchasePolicies.add(PurchasePolicy.makeServicePurchasePolicy(buispolicy));
        }
    }
    public PurchaseAndPolicy(int id)
    {
        this(new ArrayList<>(),id);
    }
}
