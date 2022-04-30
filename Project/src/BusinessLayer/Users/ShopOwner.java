package BusinessLayer.Users;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.BaseActionType;

public class ShopOwner extends ShopAdministrator{
    public ShopOwner(Shop s, SubscribedUser u) {
        super(s, u);
        for (BaseActionType b:BaseActionType.values()) {
            this.AddAction(b);
        }
    }
}
