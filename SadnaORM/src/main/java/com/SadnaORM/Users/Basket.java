package com.SadnaORM.Users;

import com.SadnaORM.Shops.Shop;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

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
    private Collection<ProductInBasket> products;


    public class BasketPKID implements Serializable {
        private Shop shop;
        private SubscribedUser user;
    }
}
