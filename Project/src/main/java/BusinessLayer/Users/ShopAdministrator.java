package BusinessLayer.Users;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.Polices.Discount.DiscountPred;
import BusinessLayer.Shops.Polices.Discount.DiscountRules;
import BusinessLayer.Shops.Polices.Discount.ProductByQuantityDiscount;
import BusinessLayer.Shops.Polices.Purchase.*;
import BusinessLayer.Users.BaseActions.*;
import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.Shop;

import javax.naming.NoPermissionException;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ShopAdministrator {
    protected Map<BaseActionType, BaseAction> action = new ConcurrentHashMap<>();
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
     *
     * @param toAssign the uset to assign to the shop manager pool
     * @return if the action complete
     * @throws NoPermissionException if the Administrator don't have a permission to the action
     */
    public boolean AssignShopManager(SubscribedUser toAssign) throws NoPermissionException {
        if (action.containsKey(BaseActionType.ASSIGN_SHOP_MANAGER))
            return ((AssignShopManager) action.get(BaseActionType.ASSIGN_SHOP_MANAGER)).act(toAssign, user.getUserName());
        else throw new NoPermissionException("you don't have permission to do that!");
    }

    public boolean AssignShopOwner(SubscribedUser toAssign) throws NoPermissionException {
        if (action.containsKey(BaseActionType.ASSIGN_SHOP_OWNER))
            return ((AssignShopOwner) action.get(BaseActionType.ASSIGN_SHOP_OWNER)).act(toAssign, user.getUserName());
        else throw new NoPermissionException("you don't have permission to do that!");
    }

    public boolean ChangeManagerPermission(SubscribedUser toAssign, Collection<BaseActionType> types) throws NoPermissionException {
        if (action.containsKey(BaseActionType.CHANGE_MANAGER_PERMISSION))
            return ((ChangeManagerPermission) action.get(BaseActionType.CHANGE_MANAGER_PERMISSION)).act(toAssign, types);
        else throw new NoPermissionException("you don't have permission to do that!");
    }

    public void removeProduct(int productid) throws NoPermissionException {
        if (action.containsKey(BaseActionType.STOCK_MANAGEMENT))
            ((StockManagement) action.get(BaseActionType.STOCK_MANAGEMENT)).removeProduct(productid);
        else throw new NoPermissionException("you don't have permission to do that!");
    }

    public Product addProduct(int productid, String name, String desc, String manufacturer, double price, int quantity) throws NoPermissionException {
        if (action.containsKey(BaseActionType.STOCK_MANAGEMENT))
            return ((StockManagement) action.get(BaseActionType.STOCK_MANAGEMENT)).addProduct(productid, name, desc, manufacturer, price, quantity);
        else throw new NoPermissionException("you don't have permission to do that!");
    }

    public boolean setCategory(int productId, String category) throws NoPermissionException {
        if (action.containsKey(BaseActionType.STOCK_MANAGEMENT))
            return ((StockManagement) action.get(BaseActionType.STOCK_MANAGEMENT)).setCategory(productId, category);
        else throw new NoPermissionException("you don't have permission to do that!");
    }

    public boolean changeProductQuantity(int productid, int newQuantity) throws NoPermissionException {
        if (action.containsKey(BaseActionType.STOCK_MANAGEMENT))
            return ((StockManagement) action.get(BaseActionType.STOCK_MANAGEMENT)).changeProductQuantity(productid, newQuantity);
        else throw new NoPermissionException("you don't have permission to do that!");
    }

    public boolean changeProductPrice(int productid, double newPrice) throws NoPermissionException {
        if (action.containsKey(BaseActionType.STOCK_MANAGEMENT))
            return ((StockManagement) action.get(BaseActionType.STOCK_MANAGEMENT)).changeProductPrice(productid, newPrice);
        else throw new NoPermissionException("you don't have permission to do that!");
    }

    public boolean changeProductDesc(int productid, String newDesc) throws NoPermissionException {
        if (action.containsKey(BaseActionType.STOCK_MANAGEMENT))
            return ((StockManagement) action.get(BaseActionType.STOCK_MANAGEMENT)).changeProductDesc(productid, newDesc);
        else throw new NoPermissionException("you don't have permission to do that!");
    }

    public boolean changeProductName(int productid, String newName) throws NoPermissionException {
        if (action.containsKey(BaseActionType.STOCK_MANAGEMENT))
            return ((StockManagement) action.get(BaseActionType.STOCK_MANAGEMENT)).changeProductName(productid, newName);
        else throw new NoPermissionException("you don't have permission to do that!");
    }

    public void addAppoint(ShopAdministrator admin) {
        if (checkForCycles(admin))
            throw new IllegalStateException("cyclic appointment!");
        appoints.add(admin);
    }

    public void AddAction(BaseActionType actionType) {
        action.put(actionType, BaseActionType.getAction(user, shop, actionType));
    }

    public void AddAction(BaseActionType actionType, BaseAction baseAction) {
        action.put(actionType, baseAction);
    }


    public Collection<BaseActionType> getActionsTypes() {
        return action.keySet();
    }

    public Collection<AdministratorInfo> getAdministratorInfo() throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.ROLE_INFO)) {
            return ((RolesInfo) action.get(BaseActionType.ROLE_INFO)).act();
        } else throw new NoPermissionException("dont hve a permission to search information about shop administrator");
    }


    public Collection<PurchaseHistory> getHistoryInfo() throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.HISTORY_INFO)) {
            return ((HistoryInfo) action.get(BaseActionType.HISTORY_INFO)).act();
        } else throw new NoPermissionException("dont hve a permission to search information about shop administrator");
    }

    public User getUser() {
        return user;
    }

    public String getUserName() {
        return user.getUserName();
    }

    public void emptyActions() {
        action = new ConcurrentHashMap<>();
    }

    public Collection<BaseAction> getPermissions() {
        return this.action.values();
    }

    public boolean checkForCycles(ShopAdministrator sa1) {
        Collection<ShopAdministrator> pool = sa1.getAppoints();
        int size = pool.size();
        while (true) {
            for (ShopAdministrator admin :
                    pool) {
                if (pool.contains(this))
                    return true;
                pool.addAll(admin.getAppoints());
            }
            if (size == pool.size())
                return false;
            size = pool.size();
        }
    }

    public Collection<ShopAdministrator> getAppoints() {
        return this.appoints;
    }

    public String getAppointer() {
        return this.appointer;
    }

    public AdministratorInfo getMyInfo() {
        return new AdministratorInfo(getUser().getUserName(), AdministratorInfo.ShopAdministratorType.MANAGER, getActionsTypes(), shop.getId(), getAppointer());
    }

    public boolean removeAdmin(SubscribedUser toRemove) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.REMOVE_ADMIN)) {
            return ((RemoveAdmin) action.get(BaseActionType.REMOVE_ADMIN)).act(toRemove);
        } else throw new NoPermissionException("don't have permission to remove appointments!");
    }

    public int createProductByQuantityDiscount(int productId, int productQuantity, double discount, int conncectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {

            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createProductByQuantityDiscount(productId, productQuantity, discount, conncectId);
        } else throw new NoPermissionException("you don't have permission to do that!");
    }

    public int createProductDiscount(int productId, double discount, int conncectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {

            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createProductDiscount(productId, discount, conncectId);
        } else throw new NoPermissionException("you don't have permission to do that!");
    }

    public int createProductQuantityInPriceDiscount(int productID, int quantity, double priceForQuantity, int conncectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {

            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createProductQuantityInPriceDiscount(productID, quantity, priceForQuantity, conncectId);
        } else throw new NoPermissionException("you don't have permission to do that!");
    }

    public int createRelatedGroupDiscount(String category, double discount, int conncectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {

            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createRelatedGroupDiscount(category, discount, conncectId);
        } else throw new NoPermissionException("you don't have permission to do that!");

    }
    public int createShopDiscount(int basketQuantity,double discount,int conncectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {

            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createShopDiscount(basketQuantity, discount, conncectId);
        } else throw new NoPermissionException("you don't have permission to do that!");

    }

    public int createDiscountAndPolicy(DiscountPred discountPred, DiscountRules discountPolicy, int conncectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {

            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createDiscountAndPolicy(discountPred, discountPolicy, conncectId);
        } else throw new NoPermissionException("you don't have permission to do that!");

    }
    public int createDiscountMaxPolicy(DiscountRules discountPolicy,int conncectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {
            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createDiscountMaxPolicy(discountPolicy, conncectId);
        } else throw new NoPermissionException("you don't have permission to do that!");
    }

    public int createDiscountOrPolicy(DiscountPred discountPred,DiscountRules discountPolicy,int conncectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {
            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createDiscountOrPolicy(discountPred, discountPolicy, conncectId);
        } else throw new NoPermissionException("you don't have permission to do that!");
    }
    public boolean removeShopOwner(SubscribedUser toRemove) throws NoPermissionException {
        if(this.action.containsKey(BaseActionType.REMOVE_SHOP_OWNER)){
            return ((RemoveShopOwner)action.get(BaseActionType.REMOVE_SHOP_OWNER)).act(toRemove);
        }
        else throw new NoPermissionException("don't have permission to remove appointments!");
    }

    public SubscribedUser getSubscribed(){
        return this.user;
    }


    public int createDiscountPlusPolicy(DiscountRules discountPolicy,int conncectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {

            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createDiscountPlusPolicy(discountPolicy, conncectId);
        } else throw new NoPermissionException("you don't have permission to do that!");

    }

    public int createDiscountXorPolicy(DiscountRules discountRules1, DiscountRules discountRules2,  DiscountPred tieBreaker,int conncectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {

            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createDiscountXorPolicy(discountRules1, discountRules2, tieBreaker, conncectId);
        } else throw new NoPermissionException("you don't have permission to do that!");

    }
    public int createValidateBasketQuantityDiscount(int basketquantity ,boolean cantBeMore,int conncectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {

            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createValidateBasketQuantityDiscount(basketquantity,cantBeMore, conncectId);
        } else throw new NoPermissionException("you don't have permission to do that!");

    }

    public int createValidateBasketValueDiscount(double basketvalue ,boolean cantBeMore,int conncectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {

            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createValidateBasketValueDiscount(basketvalue, cantBeMore, conncectId);
        } else throw new NoPermissionException("you don't have permission to do that!");

    }
    public int createValidateProductQuantityDiscount(int productId, int productQuantity, boolean cantbemore ,int conncectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {

            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createValidateProductQuantityDiscount(productId, productQuantity, cantbemore, conncectId);
        } else throw new NoPermissionException("you don't have permission to do that!");

    }

    public int createValidateProductPurchase(int productId, int productQuantity, boolean cantbemore, int conncectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {
            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createValidateProductPurchase(productId, productQuantity, cantbemore, conncectId);
        } else throw new NoPermissionException("you don't have permission to do that!");

    }

    public int createValidateTImeStampPurchase(LocalTime localTime, boolean buybefore, int connectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {
            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createValidateTImeStampPurchase(localTime, buybefore, connectId);
        } else throw new NoPermissionException("you don't have permission to do that!");
    }


    public int createValidateCategoryPurchase(String category, int productQuantity, boolean cantbemore, int connectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {
            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createValidateCategoryPurchase(category, productQuantity, cantbemore, connectId);
        } else throw new NoPermissionException("you don't have permission to do that!");
    }

    public int createValidateUserPurchase(int age, int connectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {
            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createValidateUserPurchase(age, connectId);
        } else throw new NoPermissionException("you don't have permission to do that!");

    }



    public int createPurchaseAndPolicy(PurchasePolicy policy, int conncectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {
            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createPurchaseAndPolicy(policy, conncectId);
        } else throw new NoPermissionException("you don't have permission to do that!");
    }

    public int createPurchaseOrPolicy(PurchasePolicy policy,int conncectId) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {
            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).createPurchaseOrPolicy(policy, conncectId);
        } else throw new NoPermissionException("you don't have permission to do that!");
    }

    public boolean removeDiscount(DiscountRules discountRules) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {
            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).removeDiscount(discountRules);
        } else throw new NoPermissionException("you don't have permission to do that!");

    }
    public boolean removePredicate(DiscountPred discountPred) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {
            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).removePredicate(discountPred);
        } else throw new NoPermissionException("you don't have permission to do that!");

    }
    public boolean removePurchasePolicy(PurchasePolicy purchasePolicyToDelete) throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {
            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).removePurchasePolicy(purchasePolicyToDelete);
        } else throw new NoPermissionException("you don't have permission to do that!");
    }

    public DiscountRules getDiscount() throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {
            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).getDiscount();
        } else throw new NoPermissionException("you don't have permission to do that!");
    }

    public PurchasePolicy getPurchasePolicy() throws NoPermissionException {
        if (this.action.containsKey(BaseActionType.SET_PURCHASE_POLICY)) {
            return ((SetPurchasePolicy) action.get(BaseActionType.SET_PURCHASE_POLICY)).getPurchasePolicy();
        } else throw new NoPermissionException("you don't have permission to do that!");

    }
}
