package BusinessLayer.Users;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.BaseActionType;
import BusinessLayer.Users.BaseActions.CloseShop;

import javax.naming.NoPermissionException;

public class ShopOwner extends ShopAdministrator{
    public ShopOwner(Shop s, SubscribedUser u) {
        super(s, u);
        for (BaseActionType b:BaseActionType.values()) {
            this.AddAction(b);
        }
    }

    public boolean closeShop() throws NoPermissionException {
        if(this.action.containsKey(BaseActionType.CLOSE_SHOP)){
            return ((CloseShop)action.get(BaseActionType.CLOSE_SHOP)).act();
        }
        else throw new NoPermissionException("only the founder can close the shop");
    }
}
