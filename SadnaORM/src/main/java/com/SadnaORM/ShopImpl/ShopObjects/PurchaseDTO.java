package com.SadnaORM.ShopImpl.ShopObjects;

import java.util.Collection;

public class PurchaseDTO {
    private int transactionid;

    private Collection<ProductInfoDTO> productInfos;
    private String dateOfPurchase;

    private int shop;

    public PurchaseDTO(int transactionid, Collection<ProductInfoDTO> productInfos, String dateOfPurchase, int shop, String user) {
        this.transactionid = transactionid;
        this.productInfos = productInfos;
        this.dateOfPurchase = dateOfPurchase;
        this.shop = shop;
        this.user = user;
    }

    private String user;

    public PurchaseDTO() {
    }

    public Collection<ProductInfoDTO> getProductInfos() {
        return productInfos;
    }

    public void setProductInfos(Collection<ProductInfoDTO> productInfos) {
        this.productInfos = productInfos;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public int getShop() {
        return shop;
    }

    public String getUser() {
        return user;
    }

}
