package BusinessLayer.Products.Users;

import BusinessLayer.Products.Users.BaseActions.BaseActionType;
import BusinessLayer.Shops.Shop;

public class ShopManager extends ShopAdministrator{

    public ShopManager(Shop s,SubscribedUser u) {
        super(s,u);
        this.AddAction(BaseActionType.HISTORY_INFO);
    }
}
