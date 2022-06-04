package com.SadnaORM.Shops;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "productInfo")
@IdClass(ProductInfo.ProductInfoPKID.class)
public class ProductInfo {
    @Id
    private int productID;

    @Id
    @ManyToOne
    @JoinColumn(name = "transactionID")
    private Purchase purchase;

    private int quantity;
    private int price;

    public class ProductInfoPKID implements Serializable {
        private int productID;
        private Purchase purchase;
    }
}
