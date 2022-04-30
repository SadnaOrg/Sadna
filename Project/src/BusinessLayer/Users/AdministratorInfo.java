package BusinessLayer.Users;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.BaseActionType;

import java.util.Collection;

public class AdministratorInfo {

    public enum ShopAdministratorType{
        MANAGER,
        OWNER,
        FOUNDER
    }


    private final ShopAdministratorType type;
    private final Collection<BaseActionType> permission;

    public AdministratorInfo(ShopAdministratorType type, Collection<BaseActionType> permission) {
        this.type = type;
        this.permission = permission;
    }

    public ShopAdministratorType getType() {
        return type;
    }

    public Collection<BaseActionType> getPermission() {
        return permission;
    }

}
