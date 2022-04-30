package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.BaseAction;
import BusinessLayer.Users.ShopAdministrator;
import BusinessLayer.Users.ShopOwner;
import BusinessLayer.Users.SubscribedUser;

public class AssignShopOwner extends BaseAction {
    private Shop s;
    private SubscribedUser u;

    public AssignShopOwner(Shop s, SubscribedUser u) {
        this.s = s;
        this.u = u;
    }

    public boolean act(SubscribedUser userToAssign){
        if(userToAssign.getAdministrator(s.getId())== null && s.addAdministrator(userToAssign.getUserName(), Shop.ShopAdministratorType.OWNER)){
            ShopAdministrator admin = userToAssign.addAdministrator(s.getId(), new ShopOwner(s, u, false));
            u.getAdministrator(s.getId()).addAppoint(admin);
            return true;
        }
        return false;
    }
}
