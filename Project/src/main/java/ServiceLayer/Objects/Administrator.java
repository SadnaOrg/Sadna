package ServiceLayer.Objects;

import BusinessLayer.Users.AdministratorInfo;
import BusinessLayer.Users.BaseActions.BaseActionType;

import java.util.Collection;
import java.util.LinkedList;

public class Administrator{
    private final String username;
    private final AdministratorInfo.ShopAdministratorType type;
    private final Collection<ServiceLayer.BaseActionType> permissions;
    private final int shopId;
    private final String appointer;

    public Administrator(AdministratorInfo a){
        this.username = a.getUserName();
        this.type = a.getType();
        this.permissions = convert(a.getPermission());
        this.shopId = a.getshopID();
        this.appointer = a.getAppointer();
    }

    private Collection<ServiceLayer.BaseActionType> convert(Collection<BaseActionType> actionTypes){
        Collection<ServiceLayer.BaseActionType> result = new LinkedList<>();
        for (BaseActionType type:
             actionTypes) {
            result.add(ServiceLayer.BaseActionType.lookup(type.getCode()));
        }
        return result;
    }

    public String getUsername(){
        return this.username;
    }

    public String getAppointer(){
        return this.appointer;
    }

    public int getShopId(){
        return this.shopId;
    }

    public Collection<ServiceLayer.BaseActionType> getPermissions(){
        return this.permissions;
    }

    public Type getType() {
        switch (type){
            case FOUNDER -> {
                return Type.FOUNDER;
            }
            case OWNER -> {
                return Type.OWNER;
            }
            default -> {
                return Type.MANAGER;
            }
        }
    }

    public enum Type{
        MANAGER,
        OWNER,
        FOUNDER,;
    }
}
