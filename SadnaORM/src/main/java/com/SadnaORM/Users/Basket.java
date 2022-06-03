package com.SadnaORM.Users;

import com.SadnaORM.Shops.Product;
import com.SadnaORM.Shops.Shop;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "basket")
@IdClass(Basket.BasketPKID.class)
public class Basket {
    @Id
    @ManyToOne
    @JoinColumn(name = "shopID")
    private Shop shop;

    @Id
    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "productID")
    private Product product;
    private int quantity;

    public class BasketPKID implements Serializable {
        private Shop shop;
        private User user;
        private Product product;
    }
}
