package BusinessLayer.Users;

import javax.naming.NoPermissionException;
import java.util.Map;

public class SubscribedUser extends User {
    private Map<Integer,ShopAdministrator> shopAdministrator;

    /**
     *
     * @param shop the shop id for the administrator
     * @return the administrator class for the specific shop id or null if the user isn't administrator
     */
    public ShopAdministrator getAdministrator(Integer shop){
        return shopAdministrator.getOrDefault(shop,null);
    }

    public ShopAdministrator addAdministrator(int shop, ShopAdministrator administrator) {
        if (shopAdministrator.putIfAbsent(shop,administrator)==null)
            return administrator;
        return null;
    }

    public synchronized boolean assignShopManager(int shop,SubscribedUser toAssign) throws NoPermissionException {
        if(shopAdministrator.containsKey(shop)){
            return shopAdministrator.get(shop).AssignShopManager(toAssign);
        }else
            throw new NoPermissionException("you're not the shop Administrator");
    }

}
