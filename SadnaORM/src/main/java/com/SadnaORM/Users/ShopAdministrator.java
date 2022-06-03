package com.SadnaORM.Users;

import com.SadnaORM.Shops.Shop;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@MappedSuperclass
@IdClass(ShopAdministrator.ShopAdministratorPK.class)
public abstract class ShopAdministrator {
    @ElementCollection
    @CollectionTable(
            name = "Administrator Permissions",
            joinColumns = @JoinColumn(name = "username")
    )
    private List<Action> action;
    @Id
    @ManyToOne
    @JoinColumn(name = "id")
    private Shop shop;
    @Id
    @ManyToOne
    @JoinColumn(name = "username")
    private SubscribedUser user;
    private List<ShopAdministrator> appoints;
    private String appointer;

    public class ShopAdministratorPK implements Serializable {
        private Shop shop;
        private SubscribedUser user;

    }
}

