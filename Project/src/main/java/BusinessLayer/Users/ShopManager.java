package main.java.BusinessLayer.Users;

import main.java.BusinessLayer.Shops.Shop;
import main.java.BusinessLayer.Users.BaseActions.BaseActionType;

public class ShopManager extends ShopAdministrator{

    public ShopManager(Shop s,SubscribedUser u) {
        super(s,u);
        this.AddAction(BaseActionType.HISTORY_INFO);
    }
}
