package com.SadnaORM.Users;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "ShopOwner")
public class ShopOwner extends ShopAdministrator{
    boolean isFounder;

    public ShopOwner(List<Action> action, SubscribedUser user, List<ShopAdministrator> appoints, String appointer, boolean isFounder) {
        super(action, user, appoints, appointer);
        this.isFounder = isFounder;
    }

    public ShopOwner() {

    }
}
