package com.SadnaORM.Shops;

import com.SadnaORM.Users.Basket;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "shop")
public class Shop {

    @Id
    private int id;
    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private State state = State.OPEN;

    @OneToMany(
            mappedBy = "shop",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Collection<Product> products;

    @OneToMany(
            mappedBy = "shop",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Collection<Basket> baskets;

    public Shop(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Shop() {

    }

    public enum State {
        OPEN,
        CLOSED
    }
}
