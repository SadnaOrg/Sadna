package main.java.BusinessLayer.Users;

import main.java.BusinessLayer.Shops.Shop;
import main.java.BusinessLayer.Users.BaseActions.BaseActionType;
import main.java.BusinessLayer.Users.BaseActions.CloseShop;
import main.java.BusinessLayer.Users.BaseActions.ReOpenShop;
import main.java.BusinessLayer.Users.BaseActions.RolesInfo;

import javax.naming.NoPermissionException;
import java.util.Collection;

public class ShopOwner extends ShopAdministrator{
    private boolean founder;
    public ShopOwner(Shop s, SubscribedUser u, boolean founder) {
        super(s, u);
        this.founder = founder;
        for (BaseActionType b:BaseActionType.values()) {
            if((b == BaseActionType.CLOSE_SHOP || b == BaseActionType.REOPEN_SHOP) && founder)
                this.AddAction(b);
            if((b != BaseActionType.CLOSE_SHOP && b != BaseActionType.REOPEN_SHOP))
                this.AddAction(b);
        }

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

    public boolean reOpenShop() throws Exception {
        if(founder)
            return ((ReOpenShop)action.get(BaseActionType.REOPEN_SHOP)).act();
        else throw new NoPermissionException();
    }


}
