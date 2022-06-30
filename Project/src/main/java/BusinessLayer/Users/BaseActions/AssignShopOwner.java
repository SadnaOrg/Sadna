package BusinessLayer.Users.BaseActions;

import BusinessLayer.Users.ShopOwner;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.ShopAdministrator;

public class AssignShopOwner extends BaseAction {
    private SubscribedUser u;

    public AssignShopOwner(Shop shop, SubscribedUser u) {
        super(shop);
        this.u = u;
    }

    public boolean act(SubscribedUser userToAssign, String appointer){
        ShopOwner o = new ShopOwner(shop, userToAssign,appointer, false);
        if(userToAssign.getAdministrator(shop.getId())== null && shop.addAdministrator(userToAssign.getUserName(), o)){
            ShopAdministrator admin = userToAssign.addAdministrator(shop.getId(),o);
            u.getAdministrator(shop.getId()).addAppoint(admin);
            return true;
        }
        return false;
    }

    public boolean addAdministratorToHeskemMinui(String userNameToAssign, String appointer) {
        return shop.addAdministratorToHeskemMinui(userNameToAssign,appointer);
    }
}
