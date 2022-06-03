package com.SadnaORM.Users;

import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;
import java.util.List;

@MappedSuperclass
public abstract class ShopAdministrator {
    // private Map<BaseActionType,BaseAction> action;
    // private Shop shop;
    // private SubscribedUser user;
    private List<ShopAdministrator> appoints;
    private String appointer;
    @EmbeddedId
    private ShopAdministratorPK shopAdministratorPK;
}
