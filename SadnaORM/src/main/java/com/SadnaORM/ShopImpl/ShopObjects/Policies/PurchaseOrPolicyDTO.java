package com.SadnaORM.ShopImpl.ShopObjects.Policies;

import java.util.Collection;

public class PurchaseOrPolicyDTO extends PurchasePolicyDTO{
    private Collection<PurchasePolicyDTO> purchasePolicies;

    public PurchaseOrPolicyDTO(int ID, int shopID, Collection<PurchasePolicyDTO> purchasePolicies) {
        super(ID, shopID);
        this.purchasePolicies = purchasePolicies;
    }

    public Collection<PurchasePolicyDTO> getPurchasePolicies() {
        return purchasePolicies;
    }

    public void setPurchasePolicies(Collection<PurchasePolicyDTO> purchasePolicies) {
        this.purchasePolicies = purchasePolicies;
    }
}
