package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.SubscribedUser;

import javax.naming.NoPermissionException;

public class CloseShop extends BaseAction {
    Shop shop;
    SubscribedUser user;

    public CloseShop(Shop shop, SubscribedUser user) {
        this.shop = shop;
        this.user = user;
    }

    public boolean act() throws NoPermissionException {
        if(shop.getShopAdministrator(user.getUserName())== Shop.ShopAdministratorType.FOUNDER){
            return shop.close();
        }
        else throw new NoPermissionException("only the funder can close the shop");
    }
}
