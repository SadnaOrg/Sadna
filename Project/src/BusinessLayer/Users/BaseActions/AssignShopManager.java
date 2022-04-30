package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.ShopAdministrator;
import BusinessLayer.Users.ShopManager;
import BusinessLayer.Users.SubscribedUser;

public class AssignShopManager extends BaseAction {
    private Shop s;
    private SubscribedUser u;

    public AssignShopManager(Shop s, SubscribedUser u) {
        this.s = s;
        this.u = u;
    }

    public boolean act(SubscribedUser userToAssign){
        if(userToAssign.getAdministrator(s.getId())== null && s.addAdministrator(userToAssign.getUserName(), Shop.ShopAdministratorType.MANAGER)){
            ShopAdministrator admin = userToAssign.addAdministrator(s.getId(),new ShopManager(s,u));
            u.getAdministrator(s.getId()).addAppoint(admin);
            return true;
        }
        return false;
    }

}
