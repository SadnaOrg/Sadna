package com.SadnaORM.Users;

import com.SadnaORM.Shops.Shop;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@IdClass(ShopAdministrator.ShopAdministratorPK.class)
public abstract class ShopAdministrator{
    @ElementCollection
    @CollectionTable(
            name = "AdministratorPermissions",
            joinColumns = {
                    @JoinColumn(name="ADMIN_NAME", referencedColumnName="user_username"),
                    @JoinColumn(name="SHOP_ID", referencedColumnName="shop_id")
            }
    )
    private List<Action> action;
    @Id
    @ManyToOne
    private Shop shop;
    @Id
    @ManyToOne
    private SubscribedUser user;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name="MyAppointer", referencedColumnName="user_username"),
            @JoinColumn(name="SHOP_ID", referencedColumnName="shop_id")
    })
    private List<ShopAdministrator> appoints;

    public ShopAdministrator(List<Action> action, SubscribedUser user,Shop shop, List<ShopAdministrator> appoints) {
        this.action = action;
        this.user = user;
        this.shop = shop;
        this.appoints = appoints;
    }

    public ShopAdministrator(){

    }

    public static class ShopAdministratorPK implements Serializable {
        private Shop shop;
        private SubscribedUser user;
    }
}

