package com.SadnaORM.UserImpl.UserObjects;

import java.util.List;


public class ShopManagerDTO extends ShopAdministratorDTO {
    public ShopManagerDTO() {
        super();
    }

    public ShopManagerDTO(List<BaseActionType> action, String user, int shop, int appointer) {
        super(action, user, shop, appointer);
    }
}
