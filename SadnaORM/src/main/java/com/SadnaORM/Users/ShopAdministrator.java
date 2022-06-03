package com.SadnaORM.Users;

import com.SadnaORM.Shops.Shop;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@IdClass(ShopAdministrator.ShopAdministratorPK.class)
@Table(name = "Admins")
public abstract class ShopAdministrator {
    @ElementCollection
    @CollectionTable(
            name = "Administrator Permissions",
            joinColumns = @JoinColumn(name = "username")
    )
    private List<Action> action;
    @Id
    @ManyToOne
    private Shop shop;
    @Id
    @ManyToOne
    private SubscribedUser user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appointer")
    private List<ShopAdministrator> appoints;
    @ManyToOne
    @JoinColumn(name = "username")
    private String appointer;

    public class ShopAdministratorPK implements Serializable {
        private Shop shop;
        private SubscribedUser user;

    }
}

