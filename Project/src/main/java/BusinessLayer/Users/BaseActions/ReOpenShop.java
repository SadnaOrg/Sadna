package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.ShopAdministrator;
import BusinessLayer.Users.ShopOwner;
import BusinessLayer.Users.SubscribedUser;

import javax.naming.NoPermissionException;

public class ReOpenShop extends BaseAction{
    private Shop shop;
    private SubscribedUser founder;

    public ReOpenShop(Shop shop, SubscribedUser founder){
        this.shop = shop;
        this.founder = founder;
    }

    public boolean act() throws NoPermissionException {
        ShopAdministrator admin = founder.getAdministrator(shop.getId());
        if(admin instanceof ShopOwner){
            ((ShopOwner) admin).reOpenShop();
            return true;
        }
        throw new NoPermissionException("only the founder can reopen the shop!");
    }
}
