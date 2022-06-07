package com.SadnaORM.Shops.Discounts;

import com.SadnaORM.Shops.Shop;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@IdClass(DiscountPolicy.DiscountPolicyPK.class)
public abstract class DiscountPolicy {
    @Id
    @ManyToOne
    protected Shop shop;

    @Id
    protected int ID;

    @ManyToOne
    protected DiscountPolicy policy;

    public DiscountPolicy(Shop shop, int ID, DiscountPolicy policy) {
        this.shop = shop;
        this.ID = ID;
        this.policy = policy;
    }

    public DiscountPolicy(Shop shop, int ID) {
        this.shop = shop;
        this.ID = ID;
    }

    public DiscountPolicy() {
    }

    public static class DiscountPolicyPK implements Serializable {
        private Shop shop;
        private int ID;

        public DiscountPolicyPK(Shop shop, int ID) {
            this.shop = shop;
            this.ID = ID;
        }

        public DiscountPolicyPK() {
        }
    }
}
