package com.SadnaORM.Users;

import javax.persistence.*;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "user")
public class User {
    @OneToMany(
            mappedBy = "shop",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Collection<Basket> baskets;
    @Id
    protected String name;
}
