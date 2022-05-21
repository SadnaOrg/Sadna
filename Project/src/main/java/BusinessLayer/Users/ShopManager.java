package BusinessLayer.Users;

import BusinessLayer.Users.BaseActions.BaseActionType;
import BusinessLayer.Shops.Shop;

public class ShopManager extends ShopAdministrator{

    public ShopManager(Shop s,SubscribedUser u, String appointer) {
        super(s,u, appointer);
        this.AddAction(BaseActionType.HISTORY_INFO);
    }
}
