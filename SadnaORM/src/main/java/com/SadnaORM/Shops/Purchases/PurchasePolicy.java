package com.SadnaORM.Shops.Purchases;

import com.SadnaORM.Shops.Shop;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@IdClass(PurchasePolicy.PurchasePolicyPK.class)
public abstract class PurchasePolicy {
    @Id
    @ManyToOne
    protected Shop shop;

    @Id
    protected int ID;

    protected PurchasePolicy() {
    }

    public PurchasePolicy(Shop shop, int ID) {
        this.shop = shop;
        this.ID = ID;
    }

    public Shop getShop() {
        return shop;
    }

    public int getID() {
        return ID;
    }

    public static class PurchasePolicyPK implements Serializable{
        protected Shop shop;
        protected int ID;

        public PurchasePolicyPK(Shop shop, int ID) {
            this.shop = shop;
            this.ID = ID;
        }

        public PurchasePolicyPK() {
        }
    }
}
