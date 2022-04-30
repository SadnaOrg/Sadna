package BusinessLayer.Users;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.BaseActionType;

public class ShopOwner extends ShopAdministrator{
    private boolean founder;
    public ShopOwner(Shop s, SubscribedUser u, boolean founder) {
        super(s, u);
        this.founder = founder;
        for (BaseActionType b:BaseActionType.values()) {
            this.AddAction(b);
        }
    }

    public boolean isFounder() {
        return founder;
    }
}
