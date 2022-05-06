package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.ShopAdministrator;
import BusinessLayer.Users.SubscribedUser;

import java.util.Collection;

public enum BaseActionType {
    STOCK_MANAGEMENT(1),
    SET_PURCHASE_POLICY(2),
    ASSIGN_SHOP_OWNER(4),
    ASSIGN_SHOP_MANAGER(6),
    CHANGE_MANAGER_PERMISSION(7),
    CLOSE_SHOP(9),
    ROLE_INFO(11),
    HISTORY_INFO(13);

    BaseActionType(int i) {
    }

    static public BaseAction getAction(SubscribedUser user, Shop shop, BaseActionType actionType) {
        return switch (actionType) {
            case ROLE_INFO -> new RolesInfo(shop,user);
            case CLOSE_SHOP -> new CloseShop(shop, user);
            case HISTORY_INFO -> new HistoryInfo(shop);
            case STOCK_MANAGEMENT -> new StockManagement(shop);
            case ASSIGN_SHOP_OWNER -> new AssignShopOwner(shop, user);
            case ASSIGN_SHOP_MANAGER -> new AssignShopManager(shop, user);
            case SET_PURCHASE_POLICY -> new SetPurchasePolicy();
            case CHANGE_MANAGER_PERMISSION -> new ChangeManagerPermission(shop, user);
        };
    }
}