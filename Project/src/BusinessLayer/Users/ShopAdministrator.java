package BusinessLayer.Users;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.AssignShopManager;
import BusinessLayer.Users.BaseActions.BaseAction;
import BusinessLayer.Users.BaseActions.BaseActionType;

import javax.naming.NoPermissionException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ShopAdministrator{
    protected Map<BaseActionType,BaseAction> action=new ConcurrentHashMap<>();
    protected Shop shop;
    protected SubscribedUser user;
    protected ConcurrentLinkedDeque<ShopAdministrator> appoints = new ConcurrentLinkedDeque<>();

    public ShopAdministrator(Shop s, SubscribedUser u) {
        super();
    }

    /**
     * asingn a new shop manager to the shop, only if the user has been nor manager or Owner of this shop
     * @param toAssign the uset to assign to the shop manager pool
     * @return if the action complete
     * @throws NoPermissionException if the Administrator don't have a permission to the action
     */
    public boolean AssignShopManager(SubscribedUser toAssign) throws NoPermissionException {
        if(action.containsKey(BaseActionType.ASSIGN_SHOP_MANAGER))
            return ((AssignShopManager)action.get(BaseActionType.ASSIGN_SHOP_MANAGER)).act(toAssign);
        else throw new NoPermissionException();
    }

    public void addAppoint(ShopAdministrator admin) {
        appoints.add(admin);
    }

    protected void AddAction(BaseActionType actionType){
        action.put(actionType,BaseActionType.getAction(user,shop,actionType));
    }
}
