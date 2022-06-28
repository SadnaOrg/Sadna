package BusinessLayer.Users;

import BusinessLayer.Shops.Polices.Discount.DiscountPred;
import BusinessLayer.Shops.Polices.Discount.DiscountRules;
import BusinessLayer.Shops.Polices.Purchase.PurchasePolicy;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.BaseActionType;
import BusinessLayer.Shops.PurchaseHistory;

import javax.naming.NoPermissionException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;


public class SubscribedUser extends User {
    private AtomicBoolean isNotRemoved =new AtomicBoolean(true);
    private String hashedPassword;

    public SubscribedUser(String username, boolean isNotRemoved, String hashedPassword, List<ShopAdministrator> shopAdministrator, boolean is_login, String date) {
        super(username);
        this.isNotRemoved = new AtomicBoolean(isNotRemoved);
        try {
            this.birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        }
        catch (Exception e) { System.out.println(e.getMessage()); }
        this.hashedPassword = hashedPassword;
        this.shopAdministrator = new ConcurrentHashMap<>();
        for (ShopAdministrator sa : shopAdministrator) {
            this.shopAdministrator.put(sa.getShopID(), sa);
        }
        this.is_login = is_login;
    }

    private Map<Integer,ShopAdministrator> shopAdministrator;
    private Date birthDate;
    private boolean is_login = false;

    public SubscribedUser(String userName,String password,Date birthDate) {
        super(userName);
        shopAdministrator = new ConcurrentHashMap<>();
        hashedPassword = GFG2.Hash(password);
        this.birthDate = birthDate;
    }

    public boolean login(String userName,String password) {
        if(isRemoved())
            throw new IllegalStateException("user is removed");
        if (!is_login) {
            if (Objects.equals(userName, this.getUserName()) && Objects.equals(GFG2.Hash(password), this.hashedPassword))
                is_login = true;
            return is_login;
        }
        else throw new IllegalStateException("user is allready logged in");
    }

    public boolean logout(){
        if (is_login) {
            is_login = false;
            return true;
        }
        else throw new IllegalStateException("user is already logged out");
    }

    @Override
    public boolean isLoggedIn() {
        return isNotRemoved.get() && is_login;
    }

    /**
     *
     * @param shop the shop id for the administrator
     * @return the administrator class for the specific shop id or null if the user isn't administrator
     */
    public ShopAdministrator getAdministrator(Integer shop){
        return shopAdministrator.getOrDefault(shop,null);
    }


    public String getHashedPassword() {
        return hashedPassword;
    }
    public ShopAdministrator addAdministrator(int shop, ShopAdministrator administrator) {
        if (shopAdministrator.putIfAbsent(shop,administrator)==null)
            return administrator;
        return null;
    }

    public synchronized boolean assignShopManager(int shop,SubscribedUser toAssign) throws NoPermissionException {
        validatePermission(shop);

        return shopAdministrator.get(shop).AssignShopManager(toAssign);

    }

    public Collection<ShopAdministrator> getAdministrators() {
        return shopAdministrator.values().stream().toList();
    }

    public synchronized boolean assignShopOwner(int shop, SubscribedUser toAssign) throws NoPermissionException {
        validatePermission(shop);

        return shopAdministrator.get(shop).AssignShopOwner(toAssign);

    }

    public synchronized boolean addAdministratorToHeskemMinui(int shop, String userName) throws NoPermissionException {
        validatePermission(shop);
        return shopAdministrator.get(shop).addAdministratorToHeskemMinui(userName);
    }

    public synchronized boolean approveHeskemMinui(int shop,String adminToAssign) throws NoPermissionException {
        validatePermission(shop);
        return shopAdministrator.get(shop).approveHeskemMinui(adminToAssign);
    }

    public synchronized boolean declineHeskemMinui(int shop,String adminToAssign) throws NoPermissionException {
        validatePermission(shop);
        return shopAdministrator.get(shop).declineHeskemMinui(adminToAssign);
    }

    public synchronized boolean changeManagerPermission(int shop, SubscribedUser toAssign, Collection<BaseActionType> types) throws NoPermissionException {
        validatePermission(shop);

        return shopAdministrator.get(shop).ChangeManagerPermission(toAssign, types);

    }

    public boolean closeShop(int shopId) throws NoPermissionException {
        validatePermission(shopId);

        return  ((ShopOwner)shopAdministrator.get(shopId)).closeShop();

    }

    public Collection<AdministratorInfo> getAdministratorInfo(int shopId) throws NoPermissionException {
        validatePermission(shopId);

        return shopAdministrator.get(shopId).getAdministratorInfo();

    }

    public Collection<PurchaseHistory> getHistoryInfo(int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).getHistoryInfo();

    }

    private void validatePermission(int shopId) throws NoPermissionException {
        if(isRemoved())
            throw new NoPermissionException("user is removed");
        if(!isLoggedIn())
            throw new NoPermissionException("user mast be logged in ");

        if(!shopAdministrator.containsKey(shopId))
         throw new NoPermissionException("you're not the shop Administrator");
    }

    public boolean removeAdmin(int shopID, SubscribedUser toRemove) throws NoPermissionException {
        validatePermission(shopID);
        ShopAdministrator admin = shopAdministrator.getOrDefault(shopID,null);
        return admin.removeAdmin(toRemove);
    }

    public void removeMyRole(int id) {
        shopAdministrator.remove(id);
    }

    public boolean removeShopOwner(int shopID, SubscribedUser toRemove) throws NoPermissionException {
        validatePermission(shopID);
        ShopAdministrator admin = shopAdministrator.getOrDefault(shopID,null);
        return admin.removeShopOwner(toRemove);
    }

    public void removeMyRole(int id) {
        shopAdministrator.remove(id);
    }

    public synchronized boolean removeFromSystem(){
        if(isNotRemoved.get()){
            return isNotRemoved.compareAndSet(shopAdministrator.isEmpty(),false);
        }
        else throw new IllegalArgumentException("user all ready removed");
    }

    public boolean isRemoved(){return !isNotRemoved.get();}

    public synchronized int createProductByQuantityDiscount(int productId, int productQuantity, double discount,int connectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);

        return shopAdministrator.get(shopId).createProductByQuantityDiscount(productId, productQuantity, discount, connectId);
    }


    public synchronized int createProductDiscount(int productId, double discount, int connectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);

        return shopAdministrator.get(shopId).createProductDiscount(productId, discount, connectId);
    }

    public synchronized int createProductQuantityInPriceDiscount(int productID, int quantity, double priceForQuantity, int connectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);

        return shopAdministrator.get(shopId).createProductQuantityInPriceDiscount(productID, quantity, priceForQuantity, connectId);
    }

    public synchronized int createRelatedGroupDiscount(String category, double discount, int connectId , int shopId) throws NoPermissionException {
        validatePermission(shopId);

        return shopAdministrator.get(shopId).createRelatedGroupDiscount(category, discount, connectId);
    }

    public synchronized int createShopDiscount(int basketQuantity,double discount,int connectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);

        return shopAdministrator.get(shopId).createShopDiscount(basketQuantity, discount, connectId);
    }

    public synchronized int createDiscountAndPolicy(DiscountPred discountPred, DiscountRules discountPolicy, int connectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);

        return shopAdministrator.get(shopId).createDiscountAndPolicy(discountPred, discountPolicy, connectId);
    }

    public synchronized int createDiscountMaxPolicy(DiscountRules discountPolicy,int connectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);

        return shopAdministrator.get(shopId).createDiscountMaxPolicy(discountPolicy, connectId);
    }

    public synchronized int  createDiscountOrPolicy(DiscountPred discountPred,DiscountRules discountPolicy,int connectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);

        return shopAdministrator.get(shopId).createDiscountOrPolicy(discountPred, discountPolicy, connectId);
    }

    public synchronized int  createDiscountPlusPolicy(DiscountRules discountPolicy,int connectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);

        return shopAdministrator.get(shopId).createDiscountPlusPolicy(discountPolicy, connectId);
    }

    public synchronized int createDiscountXorPolicy(DiscountRules discountRules1, DiscountRules discountRules2,  DiscountPred tieBreaker,int connectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);

        return shopAdministrator.get(shopId).createDiscountXorPolicy(discountRules1, discountRules2, tieBreaker, connectId);
    }

    public synchronized int  createValidateBasketQuantityDiscount(int basketquantity, boolean cantBeMore ,int connectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).createValidateBasketQuantityDiscount(basketquantity,cantBeMore, connectId);
    }

    public synchronized int createValidateBasketValueDiscount(double basketvalue ,boolean cantBeMore,int connectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).createValidateBasketValueDiscount(basketvalue,cantBeMore, connectId);
    }
    public synchronized int createValidateProductQuantityDiscount(int productId, int productQuantity, boolean cantbemore ,int connectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).createValidateProductQuantityDiscount(productId, productQuantity, cantbemore, connectId);
    }
    public synchronized int createValidateProductPurchase(int productId, int productQuantity, boolean cantbemore, int connectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).createValidateProductPurchase(productId, productQuantity, cantbemore, connectId);
    }

    public synchronized int createValidateTImeStampPurchase(LocalTime localTime, boolean buybefore, int conncectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).createValidateTImeStampPurchase(localTime,buybefore,conncectId);
    }

    public int createValidateDateStampPurchase(LocalDate localDate, int conncectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).createValidateDateStampPurchase(localDate, conncectId);
    }

    public synchronized int createValidateCategoryPurchase(String category, int productQuantity, boolean cantbemore, int connectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).createValidateCategoryPurchase(category, productQuantity, cantbemore, connectId);
    }

    public synchronized int createValidateUserPurchase(int age, int connectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).createValidateUserPurchase(age, connectId);
    }


    public synchronized int createPurchaseAndPolicy(PurchasePolicy policy, int conncectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).createPurchaseAndPolicy(policy, conncectId);
    }

    public synchronized int createPurchaseOrPolicy(PurchasePolicy policy, int conncectId, int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).createPurchaseOrPolicy(policy, conncectId);
    }


    public synchronized boolean removeDiscount(int ID, int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).removeDiscount(ID);
    }
    public synchronized boolean removePredicate(int ID, int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).removePredicate(ID);
    }
    public synchronized boolean removePurchasePolicy(int purchasePolicyToDelete, int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).removePurchasePolicy(purchasePolicyToDelete);
    }

    public synchronized DiscountRules getDiscount(int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).getDiscount();
    }

    public synchronized PurchasePolicy getPurchasePolicy(int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).getPurchasePolicy();
    }

    public boolean setCategory(int productId, String category, int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).setCategory(productId,category);
    }

    public synchronized boolean reOfferBid(String user,int productId, double newPrice, int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).reOfferBid(user, productId, newPrice);
    }

    public synchronized boolean declineBidOffer(String user,int productId, int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).declineBidOffer(user, productId);
    }

    public synchronized BidOffer approveBidOffer(String user, int productId, int shopId) throws NoPermissionException {
        validatePermission(shopId);
        return shopAdministrator.get(shopId).approveBidOffer(user, getUserName(), productId);
    }
    public Date getBirthDate() {
        return birthDate;
    }

    public ConcurrentHashMap<Shop, Collection<BidOffer>> getBidsToApprove() {
        var map = new ConcurrentHashMap<Shop, Collection<BidOffer>>();
        for(var s : shopAdministrator.values()){
            map.put(s.shop,s.shop.getBidsToApprove(getUserName()));
        }
        return map;
    }

    public Collection<HeskemMinui> getHeskemeyMinui() {
        var heskemim= new LinkedList<HeskemMinui>();
        for (var shop:shopAdministrator.values()) {
            heskemim.addAll(shop.getHeskemeyMinui(this));
        }
        return heskemim;
    }



// Java program to calculate SHA hash value
   private static class GFG2 {

        private static byte[] getSHA(String input)
        {
            try {
                // Static getInstance method is called with hashing SHA
                MessageDigest md = MessageDigest.getInstance("SHA-256");

                // digest() method called
                // to calculate message digest of an input
                // and return array of byte
                return md.digest(input.getBytes(StandardCharsets.UTF_8));
            }catch (NoSuchAlgorithmException e){
                return  input.getBytes(StandardCharsets.UTF_8);
            }
        }

        private static String toHexString(byte[] hash)
        {
            // Convert byte array into signum representation
            BigInteger number = new BigInteger(1, hash);

            // Convert message digest into hex value
            StringBuilder hexString = new StringBuilder(number.toString(16));

            // Pad with leading zeros
            while (hexString.length() < 64)
            {
                hexString.insert(0, '0');
            }

            return hexString.toString();
        }

        public static String Hash(String s) {
            return toHexString(getSHA(s));
        }

}
    public void setShopAdministrator(Map<Integer, ShopAdministrator> shopAdministrator) {
        this.shopAdministrator = shopAdministrator;
    }
}
