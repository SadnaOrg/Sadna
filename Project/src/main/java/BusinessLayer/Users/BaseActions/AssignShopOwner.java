package main.java.BusinessLayer.Users.BaseActions;

import main.java.BusinessLayer.Shops.Shop;
import main.java.BusinessLayer.Users.ShopAdministrator;
import main.java.BusinessLayer.Users.ShopOwner;
import main.java.BusinessLayer.Users.SubscribedUser;

public class AssignShopOwner extends BaseAction {
    private Shop s;
    private SubscribedUser u;
    private ShopOwner o;

    public AssignShopOwner(Shop s, SubscribedUser u) {
        this.s = s;
        this.u = u;
    }

    public boolean act(SubscribedUser userToAssign){
        o = new ShopOwner(s, u, false);
        if(userToAssign.getAdministrator(s.getId())== null && s.addAdministrator(userToAssign.getUserName(), o)){
            ShopAdministrator admin = userToAssign.addAdministrator(s.getId(),o);
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
