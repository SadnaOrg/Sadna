package com.SadnaORM.ShopImpl.ShopObjects.Discounts;

import com.SadnaORM.Shops.Shop;

import java.io.Serializable;

public abstract class DiscountPolicyDTO {
    protected int shop;

    protected int ID;

    protected DiscountPolicyDTO policy;

    public DiscountPolicyDTO(int shop, int ID, DiscountPolicyDTO policy) {
        this.shop = shop;
        this.ID = ID;
        this.policy = policy;
    }

    public DiscountPolicyDTO(int shop, int ID) {
        this.shop = shop;
        this.ID = ID;
    }

    public DiscountPolicyDTO() {
    }
}
