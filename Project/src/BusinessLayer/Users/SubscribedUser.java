package BusinessLayer.Users;

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
}
