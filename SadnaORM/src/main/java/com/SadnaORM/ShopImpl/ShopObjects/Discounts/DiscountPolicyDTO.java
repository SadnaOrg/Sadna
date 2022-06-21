package com.SadnaORM.ShopImpl.ShopObjects.Discounts;

public abstract class DiscountPolicyDTO {
    protected int ID;

    protected int shopID;

    public DiscountPolicyDTO(int ID, int shopID) {
        this.ID = ID;
        this.shopID = shopID;
    }

    public int getID() {
        return ID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }
}
