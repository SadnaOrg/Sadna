package BusinessLayer.Products.Users.BaseActions;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Products.Users.ShopAdministrator;
import BusinessLayer.Products.Users.ShopManager;
import BusinessLayer.Products.Users.SubscribedUser;

public class AssignShopManager extends BaseAction {
    private Shop s;
    private SubscribedUser u;

    public AssignShopManager(Shop s, SubscribedUser u) {
        this.s = s;
        this.u = u;
    }

    public boolean act(SubscribedUser userToAssign){
        ShopManager m = new ShopManager(s,u);
        if(userToAssign.getAdministrator(s.getId())== null && s.addAdministrator(userToAssign.getUserName(),m)){
            ShopAdministrator admin = userToAssign.addAdministrator(s.getId(), m);
            u.getAdministrator(s.getId()).addAppoint(admin);
            return true;
        }
        return false;
    }

}
