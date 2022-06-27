package BusinessLayer.Users.BaseActions;

import BusinessLayer.Users.ShopOwner;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Shops.Shop;

import javax.naming.NoPermissionException;

public class CloseShop extends BaseAction {
    SubscribedUser user;

    public CloseShop(Shop shop, SubscribedUser user) {
        super(shop);
        this.user = user;
    }

    public boolean act() throws NoPermissionException {
        if(user.getAdministrator(shop.getId()) instanceof ShopOwner &&((ShopOwner) user.getAdministrator(shop.getId())).isFounder()){
            return shop.close();
        }
        else throw new NoPermissionException("only the founder can close the shop");
    }
}
