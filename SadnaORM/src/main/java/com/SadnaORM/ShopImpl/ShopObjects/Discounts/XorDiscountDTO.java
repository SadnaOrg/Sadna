package com.SadnaORM.ShopImpl.ShopObjects.Discounts;

import java.util.Collection;

public class XorDiscountDTO extends DiscountPolicyDTO{
    private DiscountPolicyDTO discountRules1;
    private DiscountPolicyDTO discountRules2;
    //if all true discountRules1 else discountRules2;
    private Collection<DiscountPredDTO> tieBreakers;

    public XorDiscountDTO(int ID, int shopID, DiscountPolicyDTO discountRules1, DiscountPolicyDTO discountRules2, Collection<DiscountPredDTO> tieBreakers) {
        super(ID, shopID);
        this.discountRules1 = discountRules1;
        this.discountRules2 = discountRules2;
        this.tieBreakers = tieBreakers;
    }

    public DiscountPolicyDTO getDiscountRules1() {
        return discountRules1;
    }

    public void setDiscountRules1(DiscountPolicyDTO discountRules1) {
        this.discountRules1 = discountRules1;
    }

    public DiscountPolicyDTO getDiscountRules2() {
        return discountRules2;
    }

    public void setDiscountRules2(DiscountPolicyDTO discountRules2) {
        this.discountRules2 = discountRules2;
    }

    public Collection<DiscountPredDTO> getTieBreakers() {
        return tieBreakers;
    }

    public void setTieBreakers(Collection<DiscountPredDTO> tieBreakers) {
        this.tieBreakers = tieBreakers;
    }
}
