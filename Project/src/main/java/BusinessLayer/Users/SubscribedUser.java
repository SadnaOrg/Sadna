package main.java.BusinessLayer.Users;

import main.java.BusinessLayer.Shops.PurchaseHistory;
import main.java.BusinessLayer.Users.BaseActions.BaseActionType;

import javax.naming.NoPermissionException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


public class SubscribedUser extends User {
    private String hashedPassword;
    private Map<Integer,ShopAdministrator> shopAdministrator;
    private boolean is_login = false;

    public SubscribedUser(String userName,String password) {
        super(userName);
        shopAdministrator = new ConcurrentHashMap<>();
        hashedPassword = GFG2.Hash(password);
    }

    public boolean login(String userName,String password) {
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
        return is_login;
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

    public void removeAdministrator(int shop){
        if(shopAdministrator.containsKey(shop))
            shopAdministrator.remove(shop);
        else
            throw new IllegalArgumentException("not an administrator in that shop!");
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

    public synchronized boolean changeManagerPermission(int shop, SubscribedUser toAssign, Collection<BaseActionType> types) throws NoPermissionException {
        if(shopAdministrator.containsKey(shop)){
            return shopAdministrator.get(shop).ChangeManagerPermission(toAssign, types);
        }else
            throw new NoPermissionException("you're not the shop Administrator");
    }

    public boolean closeShop(int shopId) throws NoPermissionException {
        if(shopAdministrator.getOrDefault(shopId,null) instanceof ShopOwner){
           return  ((ShopOwner)shopAdministrator.get(shopId)).closeShop();
        }
        else throw new NoPermissionException("you're not the shop Administrator");
    }

    public Collection<AdministratorInfo> getAdministratorInfo(int shopId) throws NoPermissionException {
        if(shopAdministrator.containsKey(shopId)){
            return shopAdministrator.get(shopId).getAdministratorInfo();
        }
        else throw new NoPermissionException("you're not the shop Administrator");
    }

    public Collection<PurchaseHistory> getHistoryInfo(int shopId) throws NoPermissionException {
        if(shopAdministrator.containsKey(shopId)){
            return shopAdministrator.get(shopId).getHistoryInfo();
        }
        else throw new NoPermissionException("you're not the shop Administrator");
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




}
