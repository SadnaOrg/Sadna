package BusinessLayer.Users;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.BaseAction;
import BusinessLayer.Users.BaseActions.BaseActionType;
import BusinessLayer.Users.BaseActions.CloseShop;


import javax.naming.NoPermissionException;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ShopOwner extends ShopAdministrator{
    private boolean founder;
    public ShopOwner(Shop s, SubscribedUser u, String appointer,boolean founder) {
        super(s, u, appointer);
        this.founder = founder;
        for (BaseActionType b:BaseActionType.values()) {
            if((b == BaseActionType.CLOSE_SHOP || b == BaseActionType.REOPEN_SHOP) && founder)
                this.AddAction(b);
            if((b != BaseActionType.CLOSE_SHOP && b != BaseActionType.REOPEN_SHOP))
                this.AddAction(b);
        }

    }

    public ShopOwner(Shop s, SubscribedUser u, String appointer, boolean founder, Map<BaseActionType, BaseAction> adminActions, ConcurrentLinkedDeque<ShopAdministrator> appoints) {
        super(s, u, appointer);
        this.founder = founder;
        action = adminActions;
        this.appoints = appoints;
    }

    public boolean isFounder() {
        return founder;
    }

    public boolean closeShop() throws NoPermissionException {
        if(this.action.containsKey(BaseActionType.CLOSE_SHOP)){
            return ((CloseShop)action.get(BaseActionType.CLOSE_SHOP)).act();
        }
        else throw new NoPermissionException("only the founder can close the shop");
    }

    public void reOpenShop() throws NoPermissionException {
        if(isFounder())
            shop.open();
        else throw new NoPermissionException("only founder can reopen the shop!");
    }

    public AdministratorInfo getMyInfo() {
        AdministratorInfo.ShopAdministratorType type;
        if(isFounder())
            type = AdministratorInfo.ShopAdministratorType.FOUNDER;
        else
            type = AdministratorInfo.ShopAdministratorType.OWNER;
            return new AdministratorInfo(getUser().getUserName(),type,getActionsTypes(),shop.getId(),getAppointer());
    }
}
