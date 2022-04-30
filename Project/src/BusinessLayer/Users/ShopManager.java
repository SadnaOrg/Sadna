package BusinessLayer.Users;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.BaseActionType;

public class ShopManager extends ShopAdministrator{

    public ShopManager(Shop s,SubscribedUser u) {
        super(s,u);
        this.AddAction(BaseActionType.HISTORY_INFO);
    }
}
