package com.SadnaORM.Users;

import com.SadnaORM.Shops.Shop;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "ShopManager")
public class ShopManager extends ShopAdministrator{
    public ShopManager() {
        super();
    }

    public ShopManager(List<Action> action, SubscribedUser user, Shop shop, List<ShopAdministrator> appoints) {
        super(action, user, shop, appoints);
    }
}
