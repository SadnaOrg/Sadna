package com.SadnaORM.ShopImpl.ShopObjects.Discounts;

import java.util.Collection;

public class DiscountPlusPolicyDTO extends DiscountPolicyDTO{
    private Collection<DiscountPolicyDTO> discountPolicies;

    public DiscountPlusPolicyDTO(int ID, int shopID, Collection<DiscountPolicyDTO> discountPolicies) {
        super(ID, shopID);
        this.discountPolicies = discountPolicies;
    }

    public void setDiscountPolicies(Collection<DiscountPolicyDTO> discountPolicies) {
        this.discountPolicies = discountPolicies;
    }

    public Collection<DiscountPolicyDTO> getDiscountPolicies() {
        return discountPolicies;
    }
}
