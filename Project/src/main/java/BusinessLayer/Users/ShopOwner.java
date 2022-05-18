package BusinessLayer.Users;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.BaseActionType;
import BusinessLayer.Users.BaseActions.CloseShop;
import BusinessLayer.Users.BaseActions.RolesInfo;

import javax.naming.NoPermissionException;
import java.util.Collection;

public class ShopOwner extends ShopAdministrator{
    private boolean founder;
    public ShopOwner(Shop s, SubscribedUser u, boolean founder) {
        super(s, u);
        this.founder = founder;
        for (BaseActionType b:BaseActionType.values()) {
            if(b != BaseActionType.CLOSE_SHOP ||founder)
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


    public void reOpenShop() throws NoPermissionException {
        if(isFounder())
            shop.open();
        else throw new NoPermissionException("only founder can reopen the shop!");
    }
}
