package main.java.BusinessLayer.Users;

import main.java.BusinessLayer.Shops.PurchaseHistory;
import main.java.BusinessLayer.Shops.Shop;
import main.java.BusinessLayer.Users.BaseActions.*;

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

    public Map<BaseActionType,BaseAction> getPermissions(){
        return this.action;
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

    public boolean AssignShopOwner(SubscribedUser toAssign) throws NoPermissionException {
        if(action.containsKey(BaseActionType.ASSIGN_SHOP_OWNER))
            return ((AssignShopOwner)action.get(BaseActionType.ASSIGN_SHOP_OWNER)).act(toAssign);
        else throw new NoPermissionException();
    }

    public boolean ChangeManagerPermission(SubscribedUser toAssign, Collection<BaseActionType> types) throws NoPermissionException {
        if(action.containsKey(BaseActionType.CHANGE_MANAGER_PERMISSION))
            return ((ChangeManagerPermission)action.get(BaseActionType.CHANGE_MANAGER_PERMISSION)).act(toAssign, types);
        else throw new NoPermissionException();
    }

    public void addAppoint(ShopAdministrator admin) {
        if (!checkForCycles(admin))
            appoints.add(admin);
        else
            throw new IllegalArgumentException("a cyclic appointment has occurred");
    }

    public void AddAction(BaseActionType actionType){
        action.put(actionType,BaseActionType.getAction(user,shop,actionType));
    }

    public void AddAction(BaseActionType actionType, BaseAction baseAction){
        action.put(actionType, baseAction);
    }

    public Collection<BaseActionType> getActionsTypes() {
        return action.keySet();
    }
    public Collection<AdministratorInfo> getAdministratorInfo() throws NoPermissionException {
        if(this.action.containsKey(BaseActionType.ROLE_INFO)){
            return ((RolesInfo)action.get(BaseActionType.ROLE_INFO)).act();
        }
        else throw new NoPermissionException("don't have a permission to search information about shop administrator");
    }

    public Collection<PurchaseHistory> getHistoryInfo() throws NoPermissionException {
        if(this.action.containsKey(BaseActionType.HISTORY_INFO)){
            return ((HistoryInfo)action.get(BaseActionType.HISTORY_INFO)).act();
        }
        else throw new NoPermissionException("don't have a permission to search information about shop administrator");
    }

    public User getUser() {
        return user;
    }

    public void emptyActions(){
        action = new ConcurrentHashMap<>();
    }

    public ConcurrentLinkedDeque<ShopAdministrator> getAppoints() {
        return appoints;
    }

    // check if we can assign the given admin
    public boolean checkForCycles(ShopAdministrator administrator){
        ConcurrentLinkedDeque<ShopAdministrator> pool = administrator.getAppoints();
        int size = pool.size();
        while (true){
            if(pool.contains(this)){ // closing a cycle
                return true;
            }
            else {
                for (ShopAdministrator admin: // search next level of appointments
                        pool) {
                    pool.addAll(admin.getAppoints());
                    if (pool.contains(this)){ // closing a cycle
                        return true;
                    }
                }
                if (size == pool.size()){ // if size didn't change we are done
                    return false;
                }
                else {
                    size = pool.size(); // update size
                }
            }
        }
    }
}
