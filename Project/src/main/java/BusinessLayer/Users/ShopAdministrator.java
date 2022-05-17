package BusinessLayer.Users;

import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.*;

import javax.naming.NoPermissionException;
import java.util.Collection;
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
        shop = s;
        user = u;
    }

    /**
     * asingn a new shop manager to the shop, only if the user has been nor manager or Owner of this shop
     * @param toAssign the uset to assign to the shop manager pool
     * @return if the action complete
     * @throws NoPermissionException if the Administrator don't have a permission to the action
     */
    public boolean AssignShopManager(SubscribedUser toAssign) throws NoPermissionException {
        if(action.containsKey(BaseActionType.ASSIGN_SHOP_MANAGER))
            return ((AssignShopManager)getAction(BaseActionType.ASSIGN_SHOP_MANAGER)).act(toAssign);
        else throw new NoPermissionException();
    }

    public boolean AssignShopOwner(SubscribedUser toAssign) throws NoPermissionException {
        if(action.containsKey(BaseActionType.ASSIGN_SHOP_OWNER))
            return ((AssignShopOwner)getAction(BaseActionType.ASSIGN_SHOP_OWNER)).act(toAssign);
        else throw new NoPermissionException();
    }

    public boolean ChangeManagerPermission(SubscribedUser toAssign, Collection<BaseActionType> types) throws NoPermissionException {
        if(action.containsKey(BaseActionType.CHANGE_MANAGER_PERMISSION))
            return ((ChangeManagerPermission)getAction(BaseActionType.CHANGE_MANAGER_PERMISSION)).act(toAssign, types);
        else throw new NoPermissionException();
    }

    public void addAppoint(ShopAdministrator admin) {
        appoints.add(admin);
    }

    public void AddAction(BaseActionType actionType){
        action.put(actionType,BaseActionType.getAction(user,shop,actionType));
    }

    public Collection<BaseActionType> getActionsTypes() {
        return action.keySet();
    }
    public Collection<AdministratorInfo> getAdministratorInfo() throws NoPermissionException {
        if(this.action.containsKey(BaseActionType.ROLE_INFO)){
            return ((RolesInfo)getAction(BaseActionType.ROLE_INFO)).act();
        }
        else throw new NoPermissionException("dont hve a permission to search information about shop administrator");
    }



    public Collection<PurchaseHistory> getHistoryInfo() throws NoPermissionException {
        if(this.action.containsKey(BaseActionType.HISTORY_INFO)){
            return ((HistoryInfo)getAction(BaseActionType.HISTORY_INFO)).act();
        }
        else throw new NoPermissionException("dont hve a permission to search information about shop administrator");
    }

    private BaseAction getAction(BaseActionType actionType) {
        return action.get(actionType);
    }


    public User getUser() {
        return user;
    }

    public void emptyActions(){
        action = new ConcurrentHashMap<>();
    }
    public String getUserName()
    {
        return user.getUserName();
    }
}
