package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.BaseAction;
import BusinessLayer.Users.ShopAdministrator;
import BusinessLayer.Users.ShopManager;
import BusinessLayer.Users.ShopOwner;
import BusinessLayer.Users.SubscribedUser;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChangeManagerPermission extends BaseAction {
    private Shop s;
    private SubscribedUser u;

    public ChangeManagerPermission(Shop s, SubscribedUser u) {
        this.s = s;
        this.u = u;
    }

    public synchronized boolean act(SubscribedUser userToAssign, Collection<BaseActionType> types){
        ShopAdministrator admin = userToAssign.getAdministrator(s.getId());
        if(admin instanceof ShopManager && u.getAdministrator(s.getId()) instanceof ShopOwner){
            admin.emptyActions();
            for(BaseActionType type : types)
                admin.AddAction(type);
            return true;
        }
        return false;
    }
}
