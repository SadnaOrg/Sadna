package BusinessLayer.Users;

import BusinessLayer.Users.BaseActions.BaseActionType;

import java.util.Collection;

public class AdministratorInfo {


    public int getshopID() {
        return this.shopID;
    }

    public String getAppointer(){
        return this.appointer;
    }

    public enum ShopAdministratorType{
        MANAGER,
        OWNER,
        FOUNDER
    }

    private final String userName;
    private final int shopID;
    private final String appointer;
    private final ShopAdministratorType type;
    private final Collection<BaseActionType> permission;

    public AdministratorInfo(String userName, ShopAdministratorType type, Collection<BaseActionType> permission, int shopID, String appointer) {
        this.shopID = shopID;
        this.appointer = appointer;
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
