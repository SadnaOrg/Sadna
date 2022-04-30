package BusinessLayer.Users;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.BaseActionType;
import BusinessLayer.Users.BaseActions.CloseShop;

import javax.naming.NoPermissionException;

public class ShopOwner extends ShopAdministrator{
    private boolean founder;
    public ShopOwner(Shop s, SubscribedUser u, boolean founder) {
        super(s, u);
        this.founder = founder;
        for (BaseActionType b:BaseActionType.values()) {
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
}
