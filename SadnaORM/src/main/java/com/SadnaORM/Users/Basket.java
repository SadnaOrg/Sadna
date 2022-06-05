package com.SadnaORM.Users;

import com.SadnaORM.Shops.Shop;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

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
    @JoinColumn(name = "username")
    private SubscribedUser user;

    @ElementCollection
    @CollectionTable(
            name = "ProductsInBaskets",
            joinColumns = {
                    @JoinColumn(name = "shopID", referencedColumnName = "shopID"),
                    @JoinColumn(name = "username", referencedColumnName = "username")
            }
    )
    @MapKeyColumn(name = "PRODUCT_ID")
    @Column(name = "QUANTITY")
    private Map<Integer,Integer> products;

    public Basket() {
    }

    public Basket(Shop shop, SubscribedUser user, Map<Integer, Integer> products) {
        this.shop = shop;
        this.user = user;
        this.products = products;
    }

    public Shop getShop() {
        return shop;
    }

    public SubscribedUser getUser() {
        return user;
    }

    public Map<Integer,Integer> getProducts() {
        return products;
    }

    public static class BasketPKID implements Serializable {
        private Shop shop;
        private SubscribedUser user;

        public BasketPKID(Shop shop, SubscribedUser user) {
            this.shop = shop;
            this.user = user;
        }

        public BasketPKID(){

        }
    }
}
