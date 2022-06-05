package com.SadnaORM.Shops;

import com.SadnaORM.Users.SubscribedUser;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "purchaseHistory")
@IdClass(PurchaseHistory.PurchaseHistoryPKID.class)
public class PurchaseHistory {
    @Id
    @ManyToOne
    @JoinColumn(name = "shopID")
    private Shop shop;

    @Id
    @ManyToOne
    @JoinColumn(name = "username")
    private SubscribedUser user;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Collection<Purchase> past_purchases;

    public class PurchaseHistoryPKID implements Serializable {
        private Shop shop;
        private SubscribedUser user;
    }
}
