package com.SadnaORM.ShopImpl.ShopObjects;

import java.io.Serializable;


public class ProductInfoDTO {
    private int productID;

    private int purchaseId;
    private int quantity;
    private int price;
    public ProductInfoDTO(int productID, int purchase, int quantity, int price) {
        this.productID = productID;
        this.purchaseId = purchase;
        this.quantity = quantity;
        this.price = price;
    }

    public ProductInfoDTO() {

    }

    public class ProductInfoPKID implements Serializable {
        private int productID;
        private PurchaseDTO purchase;
    }
}
