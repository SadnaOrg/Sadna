package BusinessLayer.Users;

import BusinessLayer.Users.BaseActions.BaseAction;
import BusinessLayer.Users.BaseActions.BaseActionType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShopAdministrator extends SubscribedUser{
    protected Map<BaseActionType,BaseAction> action=new ConcurrentHashMap<>();

    
}
