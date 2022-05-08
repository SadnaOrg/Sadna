package main.java.BusinessLayer.Users.BaseActions;

import main.java.BusinessLayer.Shops.Shop;
import main.java.BusinessLayer.Users.ShopAdministrator;
import main.java.BusinessLayer.Users.ShopManager;
import main.java.BusinessLayer.Users.SubscribedUser;

public class AssignShopManager extends BaseAction {
    private Shop s;
    private SubscribedUser u;
    private ShopManager m;

    public AssignShopManager(Shop s, SubscribedUser u) {
        this.s = s;
        this.u = u;
    }

    public boolean act(SubscribedUser userToAssign){
        m = new ShopManager(s,u);
        if(userToAssign.getAdministrator(s.getId())== null && s.addAdministrator(userToAssign.getUserName(),m)){
            ShopAdministrator admin = userToAssign.addAdministrator(s.getId(), m);
            try {
                u.getAdministrator(s.getId()).addAppoint(admin);
            }
            catch (Exception e){
                userToAssign.removeAdministrator(s.getId());
                throw new IllegalStateException("cyclic appointment!");
            }
            return true;
        }
        return false;
    }
}
