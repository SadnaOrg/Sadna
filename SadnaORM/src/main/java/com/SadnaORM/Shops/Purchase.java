package com.SadnaORM.Shops;

import com.SadnaORM.Users.SubscribedUser;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "purchase")
public class Purchase {
    @Id
    private int transactionid;

    @OneToMany(
            mappedBy = "purchase",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Collection<ProductInfo> productInfos;
    private String dateOfPurchase;

    @ManyToOne
    @JoinColumn(name = "shopID")
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "username")
    private SubscribedUser user;

    public Purchase() {
    }
}
