package com.SadnaORM.ShopImpl.ShopObjects.Discounts;

import java.util.Collection;

public class OrDiscountDTO extends DiscountPolicyDTO{
    private DiscountPolicyDTO discountPolicy;
    private Collection<DiscountPredDTO> discountPreds;

    public OrDiscountDTO(int ID, int shopID, DiscountPolicyDTO discountPolicy, Collection<DiscountPredDTO> discountPreds) {
        super(ID, shopID);
        this.discountPolicy = discountPolicy;
        this.discountPreds = discountPreds;
    }

    public DiscountPolicyDTO getDiscountPolicy() {
        return discountPolicy;
    }

    public Collection<DiscountPredDTO> getDiscountPreds() {
        return discountPreds;
    }

    public void setDiscountPolicy(DiscountPolicyDTO discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public void setDiscountPreds(Collection<DiscountPredDTO> discountPreds) {
        this.discountPreds = discountPreds;
    }
}
