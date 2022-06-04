package com.SadnaORM.Users;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@IdClass(ShopAdministrator.ShopAdministratorPK.class)
public abstract class ShopAdministrator implements Serializable{
    @ElementCollection
    @CollectionTable(
            name = "Administrator Permissions",
            joinColumns = @JoinColumn(name = "username")
    )
    private List<Action> action;
//    @Id
//    @ManyToOne
//    private Shop shop;
    @Id
    @ManyToOne
    private SubscribedUser user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<ShopAdministrator> appoints;
    private String appointer;

    public ShopAdministrator(List<Action> action, SubscribedUser user, List<ShopAdministrator> appoints, String appointer) {
        this.action = action;
        this.user = user;
        this.appoints = appoints;
        this.appointer = appointer;
    }

    public ShopAdministrator(){

    }

    public class ShopAdministratorPK implements Serializable {
        // private Shop shop;
        private SubscribedUser user;

        public ShopAdministratorPK(SubscribedUser user) {
            this.user = user;
        }

        public ShopAdministratorPK(){

        }
    }
}

