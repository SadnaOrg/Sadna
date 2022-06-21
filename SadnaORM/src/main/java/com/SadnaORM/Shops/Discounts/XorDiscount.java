package com.SadnaORM.Shops.Discounts;

import com.SadnaORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;

@Entity
@Table(name = "XorDiscount")
public class XorDiscount extends DiscountPolicy{
    @ManyToOne
    private DiscountPolicy discountRules1;
    @ManyToOne
    private DiscountPolicy discountRules2;
    //if all true discountRules1 else discountRules2;
    @OneToMany
    private Collection<DiscountPred> tieBreakers;

    public XorDiscount() {
    }

    public XorDiscount(Shop shop, int ID, DiscountPolicy discountRules1, DiscountPolicy discountRules2, Collection<DiscountPred> tieBreakers) {
        super(shop, ID);
        this.discountRules1 = discountRules1;
        this.discountRules2 = discountRules2;
        this.tieBreakers = tieBreakers;
    }

    public XorDiscount(DiscountPolicy discountRules1, DiscountPolicy discountRules2, Collection<DiscountPred> tieBreakers) {
        this.discountRules1 = discountRules1;
        this.discountRules2 = discountRules2;
        this.tieBreakers = tieBreakers;
    }

    public DiscountPolicy getDiscountRules1() {
        return discountRules1;
    }

    public DiscountPolicy getDiscountRules2() {
        return discountRules2;
    }

    public Collection<DiscountPred> getTieBreakers() {
        return tieBreakers;
    }
}
