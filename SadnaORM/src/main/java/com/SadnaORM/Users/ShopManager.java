package com.SadnaORM.Users;

import com.SadnaORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "ShopManager")
public class ShopManager extends ShopAdministrator{
    public ShopManager() {
        super();
    }

    public ShopManager(List<BaseActionType> action, SubscribedUser user, Shop shop, List<ShopAdministrator> appoints) {
        super(action, user, shop, appoints);
    }
}
