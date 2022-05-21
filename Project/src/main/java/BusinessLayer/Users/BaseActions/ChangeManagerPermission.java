package BusinessLayer.Users.BaseActions;

import BusinessLayer.Users.ShopOwner;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.ShopAdministrator;
import BusinessLayer.Users.ShopManager;

import javax.naming.NoPermissionException;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ChangeManagerPermission extends BaseAction {
    private Shop s;
    private SubscribedUser u;

    public ChangeManagerPermission(Shop s, SubscribedUser u) {
        this.s = s;
        this.u = u;
    }

    public synchronized boolean act(SubscribedUser userToAssign, Collection<BaseActionType> types) throws NoPermissionException {
        ShopAdministrator admin = userToAssign.getAdministrator(s.getId());
        if (!(admin instanceof ShopManager)){
            throw new IllegalStateException("can only change permissions of a manager");
        }
        ShopAdministrator owner= u.getAdministrator(s.getId());
        if (!(owner instanceof ShopOwner))
            throw new IllegalStateException("only owners can change managers permissions!");

        ConcurrentLinkedDeque<ShopAdministrator> appoints = owner.getAppoints();
        if (!appoints.contains(admin))
            throw new NoPermissionException("can't change permissions of someone you did appoint!");

        admin.emptyActions();
        for(BaseActionType type : types)
            admin.AddAction(type);
        return true;
    }
}
