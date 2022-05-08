package main.java.BusinessLayer.Users;

import main.java.BusinessLayer.Shops.Shop;
import main.java.BusinessLayer.Users.BaseActions.BaseActionType;

import java.util.Collection;

public class AdministratorInfo {

    public enum ShopAdministratorType{
        MANAGER,
        OWNER,
        FOUNDER
    }

    private final String userName;
    private final ShopAdministratorType type;
    private final Collection<BaseActionType> permission;

    public AdministratorInfo(String userName, ShopAdministratorType type, Collection<BaseActionType> permission) {
        this.userName = userName;
        this.type = type;
        this.permission = permission;
    }

    public ShopAdministratorType getType() {
        return type;
    }

    public Collection<BaseActionType> getPermission() {
        return permission;
    }

    public String getUserName() {
        return userName;
    }
}
