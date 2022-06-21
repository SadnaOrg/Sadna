package com.SadnaORM.ShopImpl.ShopObjects.Discounts;

import java.util.Collection;

public class AndPolicyDTO extends DiscountPolicyDTO{
    private Collection<DiscountPredDTO> discountPreds;

    private DiscountPolicyDTO discountPolicy;

    public AndPolicyDTO(int ID, int shopID, Collection<DiscountPredDTO> discountPreds, DiscountPolicyDTO discountPolicy) {
        super(ID, shopID);
        this.discountPreds = discountPreds;
        this.discountPolicy = discountPolicy;
    }

    public void setDiscountPreds(Collection<DiscountPredDTO> discountPreds) {
        this.discountPreds = discountPreds;
    }

    public void setDiscountPolicy(DiscountPolicyDTO discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public Collection<DiscountPredDTO> getDiscountPreds() {
        return discountPreds;
    }

    public DiscountPolicyDTO getDiscountPolicy() {
        return discountPolicy;
    }
}
