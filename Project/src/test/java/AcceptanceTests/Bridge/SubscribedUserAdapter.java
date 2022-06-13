package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;
import ServiceLayer.BaseActionType;
import ServiceLayer.Objects.Administrator;
import ServiceLayer.Objects.AdministratorInfo;
import ServiceLayer.Objects.Policies.Discount.DiscountPred;
import ServiceLayer.Objects.Policies.Discount.DiscountRules;
import ServiceLayer.Objects.User;
import ServiceLayer.Response;
import ServiceLayer.Result;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.SystemManagerService;
import ServiceLayer.interfaces.UserService;

import java.time.LocalTime;
import java.util.*;

public class SubscribedUserAdapter extends UserAdapter implements SubscribedUserBridge{
    private Map<String, SystemManagerService> managers;
    public SubscribedUserAdapter(HashMap<String,UserService> guests, HashMap<String,SubscribedUserService> subscribed){
        super(guests, subscribed);
    }

    @Override
    public Guest logout(String userName) {
        if(subscribedUsers.containsKey(userName)){
            SubscribedUserService userService = subscribedUsers.get(userName);
            Response<UserService> guestService = userService.logout();
            UserService service = guestService.getElement();
            User guest = service.getUserInfo().getElement();
            if(guestService.isOk()){
                subscribedUsers.remove(userName);
                users.put(guest.username,service);
                return new Guest(guest.username);
            }
            else return null;
        }
        return null;
    }

    @Override
    public boolean updateProductQuantity(String username, int shopID, int productID, int newQuantity) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Result updated = service.updateProductQuantity(shopID,productID,newQuantity);
            return updated.isOk();
        }
        return false;
    }

    @Override
    public boolean updateProductPrice(String username, int shopID, int productID, double newPrice) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Result updated = service.updateProductPrice(shopID,productID,newPrice);
            return updated.isOk();
        }
        return false;
    }

    @Override
    public boolean updateProductDescription(String username, int shopID, int productID, String Desc) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Result updated = service.updateProductDescription(shopID,productID,Desc);
            return updated.isOk();
        }
        return false;
    }

    @Override
    public boolean updateProductName(String username, int shopID, int productID, String newName) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Result updated = service.updateProductName(shopID,productID,newName);
            return updated.isOk();
        }
        return false;
    }

    @Override
    public boolean deleteProductFromShop(String username, int shopID, int productID) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Result deleted = service.deleteProductFromShop(shopID,productID);
            return deleted.isOk();
        }
        return false;
    }

    @Override
    public boolean appointOwner(int shopID, String appointerName, String appointeeName) {
        if(subscribedUsers.containsKey(appointerName)){
            SubscribedUserService service = subscribedUsers.get(appointerName);
            Result appointed = service.assignShopOwner(shopID,appointeeName);
            return appointed.isOk();
        }
        return false;
    }

    @Override
    public boolean appointManager(int shopID, String appointerName, String appointeeName) {
        if(subscribedUsers.containsKey(appointerName)){
            SubscribedUserService service = subscribedUsers.get(appointerName);
            Result appointed = service.assignShopManager(shopID,appointeeName);
            return appointed.isOk();
        }
        return false;
    }

    @Override
    public boolean closeShop(int shopID, String userName) {
        if(subscribedUsers.containsKey(userName)){
            SubscribedUserService service = subscribedUsers.get(userName);
            Result closed = service.closeShop(shopID);
            return closed.isOk();
        }
        return false;
    }

    @Override
    public boolean changeAdminPermission(int shopID, String giverName, String receiverName, List<SubscribedUser.Permission> permission) {
        if(subscribedUsers.containsKey(giverName)){
            SubscribedUserService service = subscribedUsers.get(giverName);
            List<Integer> types = new LinkedList<>();
            for (SubscribedUser.Permission p:
                 permission) {
                types.add(p.getCode());
            }
            Result changed = service.changeManagerPermission(shopID,receiverName,types);
            return changed.isOk();
        }
        return false;
    }

    @Override
    public Map<String, Appointment> getShopAppointments(String requestingUsername, int shopID) {
       if(subscribedUsers.containsKey(requestingUsername)){
           SubscribedUserService service = subscribedUsers.get(requestingUsername);
           Response<AdministratorInfo> infoResponse = service.getAdministratorInfo(shopID);
           if(infoResponse.isOk()){
               AdministratorInfo info = infoResponse.getElement();
               List<Administrator> administrators = info.administrators();
               Map<String,Appointment> appointments= new HashMap<>();
               for (Administrator admin:
                    administrators) {
                   Appointment appointment = new Appointment(admin);
                   appointments.put(admin.getUsername(),appointment);
               }
               return appointments;
           }
           return null;
       }
       return null;
    }

    @Override
    public Map<String, List<SubscribedUser.Permission>> getShopPermissions(String requestingUsername, int shopID) {
        if(subscribedUsers.containsKey(requestingUsername)){
            SubscribedUserService service = subscribedUsers.get(requestingUsername);
            Response<AdministratorInfo> infoResponse = service.getAdministratorInfo(shopID);
            if(infoResponse.isOk()){
                AdministratorInfo info = infoResponse.getElement();
                List<Administrator> admins = info.administrators();
                Map<String,List<SubscribedUser.Permission>> permissionsMap = new HashMap<>();
                for (Administrator admin:
                     admins) {
                    List<SubscribedUser.Permission> permissionList = new LinkedList<>();
                    Collection<BaseActionType> actions = admin.getPermissions();
                    for (BaseActionType action:
                         actions) {
                        permissionList.add(SubscribedUser.Permission.lookup(action.getCode()));
                    }
                    permissionsMap.put(admin.getUsername(),permissionList);
                }
                return permissionsMap;
            }
            return null;
        }
        return null;
    }

    @Override
    public boolean addProductToShop(String username, int shopID, Product product, int productID, int quantity, double price) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            String desc = product.desc;
            String name = product.name;
            String manufacturer = product.manufacturer;
            Result added = service.addProductToShop(shopID,name,desc,manufacturer,productID,quantity,price);
            return added.isOk();
        }
        return false;
    }

    @Override
    public Shop openShop(String username, String name, String desc) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<ServiceLayer.Objects.Shop> newShop = service.openShop(name,desc);
            if(newShop.isOk()) {
               Shop s = new Shop(newShop.getElement());
               return s;
            }
        }
        return null;
    }

    @Override
    public boolean removeAdmin(int shopID, String requesting, String toRemove) {
        if(subscribedUsers.containsKey(requesting)){
            SubscribedUserService service = subscribedUsers.get(requesting);
            Result removed = service.removeAdmin(shopID,toRemove);
            return removed.isOk();
        }
        return false;
    }

    @Override
    public boolean reOpenShop(String username, int shopID){
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Result reopened = service.reopenShop(shopID);
            return reopened.isOk();
        }
        return false;
    }

    @Override
    public Integer createProductByQuantityDiscount(String username, int productId, int productQuantity, double discount, int connectId, int shopId) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<Integer> discountID = service.createProductByQuantityDiscount(productId,productQuantity,discount,connectId,shopId);
            if(discountID.isOk())
                return discountID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createProductDiscount(String username, int productId, double discount, int connectId, int shopId) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<Integer> discountID = service.createProductDiscount(productId,discount,connectId,shopId);
            if(discountID.isOk())
                return discountID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createProductQuantityInPriceDiscount(String username, int productID, int quantity, double priceForQuantity, int connectId, int shopId) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<Integer> discountID = service.createProductQuantityInPriceDiscount(productID,quantity,priceForQuantity,connectId,shopId);
            if(discountID.isOk())
                return discountID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createRelatedGroupDiscount(String username, String category, double discount, int connectId, int shopId) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<Integer> discountID = service.createRelatedGroupDiscount(category,discount,connectId,shopId);
            if(discountID.isOk())
                return discountID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createShopDiscount(String username, int basketQuantity, double discount, int connectId, int shopId) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<Integer> discountID = service.createShopDiscount(basketQuantity,discount,connectId,shopId);
            if(discountID.isOk())
                return discountID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createDiscountAndPolicy(String username,DiscountPred discountPred, DiscountRules discountPolicy, int connectId, int shopId) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<Integer> policyID = service.createDiscountAndPolicy(discountPred,discountPolicy,connectId,shopId);
            if(policyID.isOk())
                return policyID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createDiscountMaxPolicy(String username,DiscountRules discountPolicy, int connectId, int shopId) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<Integer> policyID = service.createDiscountMaxPolicy(discountPolicy,connectId,shopId);
            if(policyID.isOk())
                return policyID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createDiscountOrPolicy(String username,DiscountPred discountPred, DiscountRules discountPolicy, int connectId, int shopId) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<Integer> policyID = service.createDiscountOrPolicy(discountPred,discountPolicy,connectId,shopId);
            if(policyID.isOk())
                return policyID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createDiscountPlusPolicy(String username,DiscountRules discountPolicy, int connectId, int shopId) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<Integer> policyID = service.createDiscountPlusPolicy(discountPolicy,connectId,shopId);
            if(policyID.isOk())
                return policyID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createDiscountXorPolicy(String username,DiscountRules discountRules1, DiscountRules discountRules2, DiscountPred tieBreaker, int connectId, int shopId) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<Integer> policyID = service.createDiscountXorPolicy(discountRules1,discountRules2,tieBreaker,connectId,shopId);
            if(policyID.isOk())
                return policyID.getElement();
        }
        return -1;
    }


    @Override
    public Integer createValidateBasketQuantityDiscount(String username, int basketquantity, boolean cantBeMore, int connectId, int shopId) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<Integer> predID = service.createValidateBasketQuantityDiscount(basketquantity,cantBeMore,connectId,shopId);
            if(predID.isOk())
                return predID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createValidateBasketValueDiscount(String username, double basketvalue, boolean cantBeMore, int connectId, int shopId) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<Integer> predID = service.createValidateBasketValueDiscount(basketvalue,cantBeMore,connectId,shopId);
            if(predID.isOk())
                return predID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createValidateProductQuantityDiscount(String username, int productId, int productQuantity, boolean cantbemore, int connectId, int shopId) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<Integer> predID = service.createValidateProductQuantityDiscount(productId,productQuantity,cantbemore,connectId,shopId);
            if(predID.isOk())
                return predID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createValidateProductPurchase(String username, int productId, int productQuantity, boolean cantbemore, int connectId, int shopId) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<Integer> policyID = service.createValidateProductPurchase(productId,productQuantity,cantbemore,connectId,shopId);
            if(policyID.isOk())
                return policyID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createValidateTImeStampPurchase(String username, LocalTime localTime, boolean buybefore, int conncectId, int shopId) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<Integer> policyID = service.createValidateTImeStampPurchase(localTime,buybefore,conncectId,shopId);
            if(policyID.isOk())
                return policyID.getElement();
        }
        return -1;
    }

    @Override
    public boolean removeDiscount(String username,int discountID, int shopId) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<Boolean> removed = service.removeDiscount(discountID,shopId);
            if(removed.isOk())
                return removed.getElement();
        }
        return false;
    }

    @Override
    public boolean removePredicate(String username,int predicateID, int shopId) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<Boolean> removed = service.removePredicate(predicateID,shopId);
            if(removed.isOk())
                return removed.getElement();
        }
        return false;
    }

    @Override
    public boolean setCategory(String username, int productId, String category, int shopID) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Result set = service.setCategory(productId,category,shopID);
            return set.isOk();
        }
        return false;
    }
}
