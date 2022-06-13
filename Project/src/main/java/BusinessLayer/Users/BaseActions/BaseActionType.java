package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.SubscribedUser;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum BaseActionType {
    STOCK_MANAGEMENT(1),
    SET_PURCHASE_POLICY(2),
    ASSIGN_SHOP_OWNER(4),
    REMOVE_SHOP_OWNER(5),
    ASSIGN_SHOP_MANAGER(6),
    CHANGE_MANAGER_PERMISSION(7),
    CLOSE_SHOP(9),
    REOPEN_SHOP(10),
    ROLE_INFO(11),
    HISTORY_INFO(13),
    REMOVE_ADMIN(5),;

    private final int code;

    BaseActionType(int i) {
        this.code = i;
    }

    private static final Map<Integer,BaseActionType> lookup
            = new HashMap<>();

    static public BaseAction getAction(SubscribedUser user, Shop shop, BaseActionType actionType) {
        return switch (actionType) {
            case ROLE_INFO -> new RolesInfo(shop,user);
            case CLOSE_SHOP -> new CloseShop(shop, user);
            case HISTORY_INFO -> new HistoryInfo(shop);
            case STOCK_MANAGEMENT -> new StockManagement(shop);
            case ASSIGN_SHOP_OWNER -> new AssignShopOwner(shop, user);
            case ASSIGN_SHOP_MANAGER -> new AssignShopManager(shop, user);
            case SET_PURCHASE_POLICY -> new SetPurchasePolicy(shop);
            case REMOVE_SHOP_OWNER -> new RemoveShopOwner(shop,user);
            case CHANGE_MANAGER_PERMISSION -> new ChangeManagerPermission(shop, user);
            case REOPEN_SHOP -> new ReOpenShop(shop, user);
            case REMOVE_ADMIN -> new RemoveAdmin(user,shop);
        };
    }

    static {
        for(BaseActionType s : EnumSet.allOf(BaseActionType.class))
            lookup.put(s.getCode(), s);
    }

    public int getCode() {
        return this.code;
    }

    public static BaseActionType lookup(int code){
        return lookup.get(code);
    }
}