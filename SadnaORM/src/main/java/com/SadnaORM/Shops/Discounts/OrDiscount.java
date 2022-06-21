package com.SadnaORM.Shops.Discounts;

import com.SadnaORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Collection;

@Entity
@Table(name = "OrDiscount")
public class OrDiscount extends DiscountPolicy{
    @OneToOne
    private DiscountPolicy discountPolicy;
    @OneToMany
    private Collection<DiscountPred> discountPreds;

    public OrDiscount() {
    }

    public OrDiscount(Shop shop, int ID, DiscountPolicy discountPolicy, Collection<DiscountPred> discountPreds) {
        super(shop, ID);
        this.discountPolicy = discountPolicy;
        this.discountPreds = discountPreds;
    }

    public OrDiscount(DiscountPolicy discountPolicy, Collection<DiscountPred> discountPreds) {
        this.discountPolicy = discountPolicy;
        this.discountPreds = discountPreds;
    }

    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public void setDiscountPreds(Collection<DiscountPred> discountPreds) {
        this.discountPreds = discountPreds;
    }
}
