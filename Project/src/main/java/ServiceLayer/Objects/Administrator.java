package ServiceLayer.Objects;

import BusinessLayer.Products.Users.AdministratorInfo;
import BusinessLayer.Products.Users.BaseActions.BaseActionType;

import java.util.Collection;

public record Administrator(String userName, AdministratorInfo.ShopAdministratorType type, Collection<BaseActionType> permission) {

    public Administrator(AdministratorInfo a) {
        this(a.getUserName(),a.getType(),a.getPermission());
    }
}
