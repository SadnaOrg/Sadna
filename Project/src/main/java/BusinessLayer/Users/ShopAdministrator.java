package BusinessLayer.Users;

import BusinessLayer.Products.Product;
import BusinessLayer.Users.BaseActions.*;
import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.Shop;

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
    private String appointer;

    public ShopAdministrator(Shop s, SubscribedUser u, String appointer) {
        super();
        this.appointer = appointer;
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
            return ((AssignShopManager)action.get(BaseActionType.ASSIGN_SHOP_MANAGER)).act(toAssign,user.getUserName());
        else throw new NoPermissionException();
    }

    public boolean AssignShopOwner(SubscribedUser toAssign) throws NoPermissionException {
        if(action.containsKey(BaseActionType.ASSIGN_SHOP_OWNER))
            return ((AssignShopOwner)action.get(BaseActionType.ASSIGN_SHOP_OWNER)).act(toAssign,user.getUserName());
        else throw new NoPermissionException();
    }

    public boolean ChangeManagerPermission(SubscribedUser toAssign, Collection<BaseActionType> types) throws NoPermissionException {
        if(action.containsKey(BaseActionType.CHANGE_MANAGER_PERMISSION))
            return ((ChangeManagerPermission)action.get(BaseActionType.CHANGE_MANAGER_PERMISSION)).act(toAssign, types);
        else throw new NoPermissionException();
    }

    public void removeProduct(int productid) throws NoPermissionException {
        if(action.containsKey(BaseActionType.STOCK_MANAGEMENT))
            ((StockManagement)action.get(BaseActionType.STOCK_MANAGEMENT)).removeProduct(productid);
        else throw new NoPermissionException();
    }
    public Product addProduct(int productid, String name, String desc,String manufacturer, double price, int quantity) throws NoPermissionException {
        if(action.containsKey(BaseActionType.STOCK_MANAGEMENT))
            return ((StockManagement)action.get(BaseActionType.STOCK_MANAGEMENT)).addProduct(productid, name, desc, manufacturer, price, quantity);
        else throw new NoPermissionException();
    }

    public boolean changeProductQuantity(int productid, int newQuantity) throws NoPermissionException {
        if(action.containsKey(BaseActionType.STOCK_MANAGEMENT))
            return ((StockManagement)action.get(BaseActionType.STOCK_MANAGEMENT)).changeProductQuantity(productid, newQuantity);
        else throw new NoPermissionException();
    }

    public boolean changeProductPrice(int productid, double newPrice) throws NoPermissionException {
        if(action.containsKey(BaseActionType.STOCK_MANAGEMENT))
            return ((StockManagement)action.get(BaseActionType.STOCK_MANAGEMENT)).changeProductPrice(productid, newPrice);
        else throw new NoPermissionException();
    }

    public boolean changeProductDesc(int productid, String newDesc) throws NoPermissionException {
        if(action.containsKey(BaseActionType.STOCK_MANAGEMENT))
            return ((StockManagement)action.get(BaseActionType.STOCK_MANAGEMENT)).changeProductDesc(productid, newDesc);
        else throw new NoPermissionException();
    }

    public boolean changeProductName(int productid, String newName) throws NoPermissionException {
        if(action.containsKey(BaseActionType.STOCK_MANAGEMENT))
            return ((StockManagement)action.get(BaseActionType.STOCK_MANAGEMENT)).changeProductName(productid, newName);
        else throw new NoPermissionException();
    }


    public void addAppoint(ShopAdministrator admin) {
        if(checkForCycles(admin))
            throw new IllegalStateException("cyclic appointment!");
        appoints.add(admin);
    }

    public void AddAction(BaseActionType actionType){
        action.put(actionType,BaseActionType.getAction(user,shop,actionType));
    }

    public void AddAction(BaseActionType actionType, BaseAction baseAction){
        action.put(actionType,baseAction);
    }


    public Collection<BaseActionType> getActionsTypes() {
        return action.keySet();
    }
    public Collection<AdministratorInfo> getAdministratorInfo() throws NoPermissionException {
        if(this.action.containsKey(BaseActionType.ROLE_INFO)){
            return ((RolesInfo)action.get(BaseActionType.ROLE_INFO)).act();
        }
        else throw new NoPermissionException("dont hve a permission to search information about shop administrator");
    }



    public Collection<PurchaseHistory> getHistoryInfo() throws NoPermissionException {
        if(this.action.containsKey(BaseActionType.HISTORY_INFO)){
            return ((HistoryInfo)action.get(BaseActionType.HISTORY_INFO)).act();
        }
        else throw new NoPermissionException("dont hve a permission to search information about shop administrator");
    }

    public User getUser() {
        return user;
    }

    public String getUserName() {return user.getUserName(); }

    public void emptyActions(){
        action = new ConcurrentHashMap<>();
    }

    public Collection<BaseAction> getPermissions() {
        return this.action.values();
    }

    public boolean checkForCycles(ShopAdministrator sa1) {
        ConcurrentLinkedDeque<ShopAdministrator> pool = sa1.getAppoints();
        int size = pool.size();
        while (true){
            for (ShopAdministrator admin:
                 pool) {
                if(pool.contains(this))
                    return true;
                pool.addAll(admin.getAppoints());
            }
            if(size == pool.size())
                return false;
            size = pool.size();
        }
    }

    public ConcurrentLinkedDeque<ShopAdministrator> getAppoints() {
        return this.appoints;
    }

    public String getAppointer() {
        return this.appointer;
    }

    public AdministratorInfo getMyInfo() {
        return new AdministratorInfo(getUser().getUserName(),AdministratorInfo.ShopAdministratorType.MANAGER,getActionsTypes(),shop.getId(),getAppointer());
    }

    public boolean removeAdmin(SubscribedUser toRemove) throws NoPermissionException {
        if(this.action.containsKey(BaseActionType.REMOVE_ADMIN)){
            return ((RemoveAdmin)action.get(BaseActionType.REMOVE_ADMIN)).act(toRemove);
        }
        else throw new NoPermissionException("don't have permission to remove appointments!");
    }

    public SubscribedUser getSubscribed(){return this.user;}
}
