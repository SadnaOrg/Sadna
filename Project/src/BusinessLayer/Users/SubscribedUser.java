package BusinessLayer.Users;

import BusinessLayer.Users.BaseActions.BaseActionType;
import BusinessLayer.Users.BaseActions.CloseShop;

import javax.naming.NoPermissionException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SubscribedUser extends User {
    private Map<Integer,ShopAdministrator> shopAdministrator;

    public SubscribedUser(String userName) {
        super(userName);
        shopAdministrator = new ConcurrentHashMap<>();
    }

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

    public synchronized boolean assignShopOwner(int shop, SubscribedUser toAssign) throws NoPermissionException {
        if(shopAdministrator.containsKey(shop)){
            return shopAdministrator.get(shop).AssignShopOwner(toAssign);
        }else
            throw new NoPermissionException("you're not the shop Administrator");
    }

    public synchronized boolean changeManagerPermission(int shop, SubscribedUser toAssign, BaseActionType[] types) throws NoPermissionException {
        if(shopAdministrator.containsKey(shop)){
            return shopAdministrator.get(shop).ChangeManagerPermission(toAssign, types);
        }else
            throw new NoPermissionException("you're not the shop Administrator");
    }

    public boolean closeShop(int shopId) throws NoPermissionException {
        if(shopAdministrator.getOrDefault(shopId,null)instanceof ShopOwner){
           return  ((ShopOwner)shopAdministrator.get(shopId)).closeShop();
        }
        else throw new NoPermissionException("you're not the shop Administrator");
    }

}
