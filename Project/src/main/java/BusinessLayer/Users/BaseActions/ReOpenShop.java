package BusinessLayer.Users.BaseActions;

import BusinessLayer.Notifications.ConcreteNotification;
import BusinessLayer.System.System;
import BusinessLayer.Users.ShopOwner;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.ShopAdministrator;

import javax.naming.NoPermissionException;
import java.util.stream.Collectors;

public class ReOpenShop extends BaseAction{
    private SubscribedUser founder;

    public ReOpenShop(Shop shop, SubscribedUser founder){
        super(shop);
        this.founder = founder;
    }

    public boolean act() throws NoPermissionException {
        ShopAdministrator admin = founder.getAdministrator(shop.getId());
        if(admin instanceof ShopOwner){
            ((ShopOwner) admin).reOpenShop();
            System.getInstance().getNotifier().addNotification(new ConcreteNotification(
                    shop.getShopAdministrators().stream().filter(sa -> sa instanceof ShopOwner).map(sa ->sa.getUserName()).collect(Collectors.toList())
                    ,admin.getUserName()+ ": re-open shop "+ shop.getName()+" with id " +shop.getId()));
            return true;
        }
        throw new NoPermissionException("only the founder can reopen the shop!");
    }
}
