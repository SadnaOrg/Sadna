package main.java.BusinessLayer.Users.BaseActions;

import main.java.BusinessLayer.Shops.Shop;
import main.java.BusinessLayer.Users.ShopAdministrator;
import main.java.BusinessLayer.Users.ShopManager;
import main.java.BusinessLayer.Users.ShopOwner;
import main.java.BusinessLayer.Users.SubscribedUser;

import java.util.Collection;

public class ChangeManagerPermission extends BaseAction {
    private Shop s;
    private SubscribedUser u;

    public ChangeManagerPermission(Shop s, SubscribedUser u) {
        this.s = s;
        this.u = u;
    }

    public synchronized boolean act(SubscribedUser userToAssign, Collection<BaseActionType> types){
        ShopAdministrator admin = userToAssign.getAdministrator(s.getId());
        ShopAdministrator userAdmin = u.getAdministrator(s.getId());
        boolean appointed = userAdmin.getAppoints().contains(admin);
        if(admin instanceof ShopManager && userAdmin instanceof ShopOwner && appointed){
            admin.emptyActions();
            for(BaseActionType type : types)
                admin.AddAction(type);
            return true;
        }
        if (!appointed){
            throw new IllegalArgumentException("can't change permissions of someone you didn't appoint!");
        }
        else if (!(userAdmin instanceof ShopOwner)){
            throw new IllegalArgumentException("only owners can change the permissions of managers!");
        }
        else {
            throw new IllegalArgumentException("tried to change permission of a non owner administrator!");
        }
    }
}
