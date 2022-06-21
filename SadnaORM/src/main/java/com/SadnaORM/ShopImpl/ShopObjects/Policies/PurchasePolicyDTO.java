package com.SadnaORM.ShopImpl.ShopObjects.Policies;

public abstract class PurchasePolicyDTO {
    protected int ID;

    protected int shopID;

    public PurchasePolicyDTO(int ID, int shopID) {
        this.ID = ID;
        this.shopID = shopID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }
}
