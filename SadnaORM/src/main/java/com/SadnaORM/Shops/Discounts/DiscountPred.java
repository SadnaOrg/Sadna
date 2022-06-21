package com.SadnaORM.Shops.Discounts;

import com.SadnaORM.Shops.Shop;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@IdClass(DiscountPolicy.DiscountPolicyPK.class)
public abstract class DiscountPred {
    @Id
    @ManyToOne
    protected Shop shop;

    @Id
    protected int ID;

    public DiscountPred(Shop shop, int ID) {
        this.shop = shop;
        this.ID = ID;
    }

    public DiscountPred() {
    }

    public Shop getShop() {
        return shop;
    }

    public int getID() {
        return ID;
    }

    public static class DiscountPredPK implements Serializable {
        private Shop shop;
        private int ID;

        public DiscountPredPK(Shop shop, int ID) {
            this.shop = shop;
            this.ID = ID;
        }

        public DiscountPredPK() {
        }
    }
}
