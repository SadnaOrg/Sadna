package com.SadnaORM.Users;

import com.SadnaORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
@Entity
@Table(name = "ShopOwner")
public class ShopOwner extends ShopAdministrator{
    boolean isFounder;

    public ShopOwner(List<BaseActionType> action, SubscribedUser user, Shop shop, List<ShopAdministrator> appoints, boolean isFounder) {
        super(action, user, shop, appoints);
        this.isFounder = isFounder;
    }

    public ShopOwner() {
        super();
    }
}
