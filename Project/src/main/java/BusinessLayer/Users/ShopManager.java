package BusinessLayer.Users;

import BusinessLayer.Users.BaseActions.BaseAction;
import BusinessLayer.Users.BaseActions.BaseActionType;
import BusinessLayer.Shops.Shop;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ShopManager extends ShopAdministrator{

    public ShopManager(Shop s,SubscribedUser u, String appointer) {
        super(s,u, appointer);
        this.AddAction(BaseActionType.HISTORY_INFO);
    }

    public ShopManager(Map<BaseActionType, BaseAction> adminActions, SubscribedUser u, Shop s, ConcurrentLinkedDeque<ShopAdministrator> appoints, String appointer) {
        super(s, u, appointer);
        action = adminActions;
        this.appoints = appoints;
    }
}
