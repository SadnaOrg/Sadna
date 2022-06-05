package com.SadnaORM.Shops;

import com.SadnaORM.Users.Basket;
import com.SadnaORM.Users.SubscribedUser;

import javax.persistence.*;
import java.util.Collection;
import java.util.Map;

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
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Collection<Product> products;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "userBaskets",
            inverseJoinColumns = {
            @JoinColumn(name = "basketShopID", referencedColumnName = "shopID"),
                    @JoinColumn(name = "basketOwner", referencedColumnName = "username")
    })
    @MapKeyJoinColumn(name = "username")
    private Map<SubscribedUser, Basket> usersBaskets;

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
