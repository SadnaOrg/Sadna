package com.SadnaORM.UserImpl.UserObjects;

import java.io.Serializable;
import java.util.List;
public class ShopOwnerDTO extends ShopAdministratorDTO implements Serializable {
    boolean isFounder;

    public ShopOwnerDTO(List<BaseActionType> action, String user, int shop, boolean isFounder) {
        super(action, user, shop);
        this.isFounder = isFounder;
    }

    public ShopOwnerDTO(List<BaseActionType> action, String user, int shop, int appointer, boolean isFounder) {
        super(action, user, shop, appointer);
        this.isFounder = isFounder;
    }

    public ShopOwnerDTO() {
        super();
    }

    public boolean isFounder() {
        return isFounder;
    }
}
