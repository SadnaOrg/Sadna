package com.SadnaORM.Shops.Purchases;

import com.SadnaORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PurchaseGriraPolicy")
public class PurchaseGriraPolicy extends PurchasePolicy{
    @ManyToOne
    private PurchasePolicy purchasePolicyAllow;
    @ManyToOne
    private PurchasePolicy validatePurchase;

    public PurchaseGriraPolicy() {
    }

    public PurchaseGriraPolicy(PurchasePolicy purchasePolicyAllow, PurchasePolicy validatePurchase) {
        this.purchasePolicyAllow = purchasePolicyAllow;
        this.validatePurchase = validatePurchase;
    }

    public PurchaseGriraPolicy(Shop shop, int ID, PurchasePolicy purchasePolicyAllow, PurchasePolicy validatePurchase) {
        super(shop, ID);
        this.purchasePolicyAllow = purchasePolicyAllow;
        this.validatePurchase = validatePurchase;
    }

    public PurchasePolicy getPurchasePolicyAllow() {
        return purchasePolicyAllow;
    }

    public PurchasePolicy getValidatePurchase() {
        return validatePurchase;
    }
}
