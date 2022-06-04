package com.SadnaORM.Users;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "ShopManager")
public class ShopManager extends ShopAdministrator{
    public ShopManager() {
        super();
    }

    public ShopManager(List<Action> action, SubscribedUser user, List<ShopAdministrator> appoints, String appointer) {
        super(action, user, appoints, appointer);
    }
}
