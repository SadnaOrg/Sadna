package com.SadnaORM.ShopImpl.ShopObjects.Discounts;


import java.util.Collection;

public class MaxDiscountPolicyDTO extends DiscountPolicyDTO{
    private Collection<DiscountPolicyDTO> discountPolicies;

    public MaxDiscountPolicyDTO(int ID, int shopID, Collection<DiscountPolicyDTO> discountPolicies) {
        super(ID, shopID);
        this.discountPolicies = discountPolicies;
    }

    public Collection<DiscountPolicyDTO> getDiscountPolicies() {
        return discountPolicies;
    }

    public void setDiscountPolicies(Collection<DiscountPolicyDTO> discountPolicies) {
        this.discountPolicies = discountPolicies;
    }
}
