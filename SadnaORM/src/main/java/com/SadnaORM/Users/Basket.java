package com.SadnaORM.Users;

import com.SadnaORM.Shops.Shop;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
@Data
@Entity
@Table(name = "basket")
@IdClass(Basket.BasketPKID.class)
public class Basket {
    @Id
    @ManyToOne
    @JoinColumn(name = "shopID")
    private Shop shop;
    @Id
    private int id;

    @ElementCollection
    @CollectionTable(
            name = "ProductsInBaskets",
            joinColumns = {
                    @JoinColumn(name = "shopID", referencedColumnName = "shopID"),
                    @JoinColumn(name = "username", referencedColumnName = "id")
            }
    )
    private Collection<ProductInBasket> products;


    public class BasketPKID implements Serializable {
        private Shop shop;
        private int id;
    }
}
