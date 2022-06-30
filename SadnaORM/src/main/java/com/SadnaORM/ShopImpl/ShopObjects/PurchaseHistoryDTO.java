package com.SadnaORM.ShopImpl.ShopObjects;

import java.util.Collection;

public class PurchaseHistoryDTO {
    public PurchaseHistoryDTO(int shop, String user) {
        this.shop = shop;
        this.user = user;
    }

    private int shop;
    private String user;

    private Collection<PurchaseDTO> past_purchases;

    public PurchaseHistoryDTO() {

    }

    public Collection<PurchaseDTO> getPast_purchases() {
        return past_purchases;
    }

    public void setPast_purchases(Collection<PurchaseDTO> past_purchases) {
        this.past_purchases = past_purchases;
    }
}
