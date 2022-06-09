package BusinessLayer.Users;

import BusinessLayer.Shops.Polices.Discount.DiscountPred;
import BusinessLayer.Shops.Polices.Discount.DiscountRules;
import BusinessLayer.Users.BaseActions.BaseActionType;
import BusinessLayer.Shops.PurchaseHistory;

import javax.naming.NoPermissionException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;


public class SubscribedUser extends User {
    private AtomicBoolean isNotRemoved =new AtomicBoolean(true);
    private String hashedPassword;
    private Map<Integer,ShopAdministrator> shopAdministrator;
    private boolean is_login = false;

    public SubscribedUser(String userName,String password) {
        super(userName);
        shopAdministrator = new ConcurrentHashMap<>();
        hashedPassword = GFG2.Hash(password);
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

    public ShopAdministrator addAdministrator(int shop, ShopAdministrator administrator) {
        if (shopAdministrator.putIfAbsent(shop,administrator)==null)
            return administrator;
        return null;
    }

    public synchronized boolean assignShopManager(int shop,SubscribedUser toAssign) throws NoPermissionException {
        validatePermission(shop);

        return shopAdministrator.get(shop).AssignShopManager(toAssign);

    }

    public synchronized boolean assignShopOwner(int shop, SubscribedUser toAssign) throws NoPermissionException {
        validatePermission(shop);

        return shopAdministrator.get(shop).AssignShopOwner(toAssign);

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

    public synchronized boolean removeFromSystem(){
        if(isNotRemoved.get()){
            return isNotRemoved.compareAndSet(shopAdministrator.isEmpty(),false);
        }
        else throw new IllegalArgumentException("user all ready removed");
    }

    public boolean isRemoved(){return !isNotRemoved.get();}
//
//    public synchronized boolean createProductByQuantityDiscount(int productId, int productQuantity, double discount,int conncectId, int shopId) throws NoPermissionException {
//        validatePermission(shopId);
//
//        return shopAdministrator.get(shopId).createProductByQuantityDiscount(productId, productQuantity, discount, conncectId);
//    }
//
//
//    public synchronized boolean createProductDiscount(int productId, double discount, int conncectId, int shopId) throws NoPermissionException {
//        validatePermission(shopId);
//
//        return shopAdministrator.get(shopId).createProductDiscount(productId, discount, conncectId);
//    }
//
//    public synchronized boolean createProductQuantityInPriceDiscount(int productID, int quantity, double priceForQuantity, int conncectId, int shopId) throws NoPermissionException {
//        validatePermission(shopId);
//
//        return shopAdministrator.get(shopId).createProductQuantityInPriceDiscount(productID, quantity, priceForQuantity, conncectId);
//    }
//
//    public synchronized boolean createRelatedGroupDiscount(Collection<Integer> relatedProducts, double discount, int conncectId , int shopId) throws NoPermissionException {
//        validatePermission(shopId);
//
//        return shopAdministrator.get(shopId).createRelatedGroupDiscount(relatedProducts, discount, conncectId);
//    }
//
//    public synchronized boolean createShopDiscount(int basketQuantity,double discount,int conncectId, int shopId) throws NoPermissionException {
//        validatePermission(shopId);
//
//        return shopAdministrator.get(shopId).createShopDiscount(basketQuantity, discount, conncectId);
//    }
//
//    public synchronized boolean createDiscountAndPolicy(DiscountPred discountPred, DiscountRules discountPolicy, int conncectId, int shopId) throws NoPermissionException {
//        validatePermission(shopId);
//
//        return shopAdministrator.get(shopId).createDiscountAndPolicy(discountPred, discountPolicy, conncectId);
//    }
//
//    public synchronized boolean createDiscountMaxPolicy(DiscountRules discountPolicy,int conncectId, int shopId) throws NoPermissionException {
//        validatePermission(shopId);
//
//        return shopAdministrator.get(shopId).createDiscountMaxPolicy(discountPolicy, conncectId);
//    }
//
//    public synchronized boolean  createDiscountOrPolicy(DiscountPred discountPred,DiscountRules discountPolicy,int conncectId, int shopId) throws NoPermissionException {
//        validatePermission(shopId);
//
//        return shopAdministrator.get(shopId).createDiscountOrPolicy(discountPred, discountPolicy, conncectId);
//    }
//
//    public synchronized boolean  createDiscountPlusPolicy(DiscountRules discountPolicy,int conncectId, int shopId) throws NoPermissionException {
//        validatePermission(shopId);
//
//        return shopAdministrator.get(shopId).createDiscountPlusPolicy(discountPolicy, conncectId);
//    }
//
//    public synchronized boolean createDiscountXorPolicy(DiscountRules discountRules1, DiscountRules discountRules2,  DiscountPred tieBreaker,int conncectId, int shopId) throws NoPermissionException {
//        validatePermission(shopId);
//
//        return shopAdministrator.get(shopId).createDiscountXorPolicy(discountRules1, discountRules2, tieBreaker, conncectId);
//    }
//
//    public synchronized boolean  createValidateBasketQuantityDiscount(int basketquantity ,int conncectId, int shopId) throws NoPermissionException {
//        validatePermission(shopId);
//
//        return shopAdministrator.get(shopId).createValidateBasketQuantityDiscount(basketquantity, conncectId);
//    }
//
//    public synchronized boolean createValidateBasketValueDiscount(double basketvalue ,int conncectId, int shopId) throws NoPermissionException {
//        validatePermission(shopId);
//
//        return shopAdministrator.get(shopId).createValidateBasketValueDiscount(basketvalue, conncectId);
//    }
//    public synchronized boolean createValidateProductQuantityDiscount(int productId, int productQuantity, boolean cantbemore ,int conncectId, int shopId) throws NoPermissionException {
//        validatePermission(shopId);
//
//        return shopAdministrator.get(shopId).createValidateProductQuantityDiscount(productId, productQuantity, cantbemore, conncectId);
//    }
//
//

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
}
