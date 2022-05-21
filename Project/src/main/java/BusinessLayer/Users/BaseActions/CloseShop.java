package BusinessLayer.Products.Users.BaseActions;

import BusinessLayer.Products.Users.ShopOwner;
import BusinessLayer.Products.Users.SubscribedUser;
import BusinessLayer.Shops.Shop;

import javax.naming.NoPermissionException;

public class CloseShop extends BaseAction {
    Shop shop;
    SubscribedUser user;

    public CloseShop(Shop shop, SubscribedUser user) {
        this.shop = shop;
        this.user = user;
    }

    public boolean act() throws NoPermissionException {
        if(user.getAdministrator(shop.getId()) instanceof ShopOwner &&((ShopOwner) user.getAdministrator(shop.getId())).isFounder()){
            return shop.close();
        }
        else throw new NoPermissionException("only the founder can close the shop");
    }
}
