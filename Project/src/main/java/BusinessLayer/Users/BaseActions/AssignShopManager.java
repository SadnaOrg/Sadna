package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.ShopAdministrator;
import BusinessLayer.Users.ShopManager;
import BusinessLayer.Users.SubscribedUser;

public class AssignShopManager extends BaseAction {
    private SubscribedUser u;

    public AssignShopManager(Shop shop, SubscribedUser u) {
        super(shop);
        this.u = u;
    }

    public boolean act(SubscribedUser userToAssign, String appointer){
        ShopManager m = new ShopManager(shop,userToAssign,appointer);
        if(userToAssign.getAdministrator(shop.getId())== null && shop.addAdministrator(userToAssign.getUserName(),m)){
            ShopAdministrator admin = userToAssign.addAdministrator(shop.getId(), m);
            u.getAdministrator(shop.getId()).addAppoint(admin);
            return true;
        }
        return false;
    }

}
