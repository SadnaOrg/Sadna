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
    public SubscribedUserAdapter(HashMap<String,UserService> guests, HashMap<String,SubscribedUserService> subscribed,HashMap<String,List<Notification>> notifications){
        super(guests, subscribed,notifications);
        managers = new HashMap<>();
    }

    @Override
    public Guest logout(String userName) {
        SubscribedUserService service = getMyService(userName);
        if(service!= null){
            Response<UserService> guestService = service.logout();
            UserService Guestservice = guestService.getElement();
            User guest = Guestservice.getUserInfo().getElement();
            if(guestService.isOk()){
                if(subscribedUsers.containsKey(userName))
                    subscribedUsers.remove(userName);
                else managers.remove(userName);
                users.put(guest.username,Guestservice);
                return new Guest(guest.username);
            }
            else return null;
        }
        return null;
    }

    @Override
    public boolean updateProductQuantity(String username, int shopID, int productID, int newQuantity) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Result updated = service.updateProductQuantity(shopID,productID,newQuantity);
            return updated.isOk();
        }
        return false;
    }

    @Override
    public boolean updateProductPrice(String username, int shopID, int productID, double newPrice) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Result updated = service.updateProductPrice(shopID,productID,newPrice);
            return updated.isOk();
        }
        return false;
    }

    @Override
    public boolean updateProductDescription(String username, int shopID, int productID, String Desc) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Result updated = service.updateProductDescription(shopID,productID,Desc);
            return updated.isOk();
        }
        return false;
    }

    @Override
    public boolean updateProductName(String username, int shopID, int productID, String newName) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Result updated = service.updateProductName(shopID,productID,newName);
            return updated.isOk();
        }
        return false;
    }

    @Override
    public boolean deleteProductFromShop(String username, int shopID, int productID) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Result deleted = service.deleteProductFromShop(shopID,productID);
            return deleted.isOk();
        }
        return false;
    }

    @Override
    public boolean appointOwner(int shopID, String appointerName, String appointeeName) {
        SubscribedUserService service = getMyService(appointerName);
        if(service != null){
            Result appointed = service.assignShopOwner(shopID,appointeeName);
            return appointed.isOk();
        }
        return false;
    }

    @Override
    public boolean appointManager(int shopID, String appointerName, String appointeeName) {
        SubscribedUserService service = getMyService(appointerName);
        if(service != null){
            Result appointed = service.assignShopManager(shopID,appointeeName);
            return appointed.isOk();
        }
        return false;
    }

    @Override
    public boolean closeShop(int shopID, String userName) {
        SubscribedUserService service = getMyService(userName);
        if(service!= null){
            Result closed = service.closeShop(shopID);
            return closed.isOk();
        }
        return false;
    }

    @Override
    public boolean changeAdminPermission(int shopID, String giverName, String receiverName, List<SubscribedUser.Permission> permission) {
        SubscribedUserService service = getMyService(giverName);
        if(subscribedUsers.containsKey(giverName)){
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
        SubscribedUserService service = getMyService(requestingUsername);
       if(service != null){
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
        SubscribedUserService service = getMyService(requestingUsername);
        if(service != null){
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
        SubscribedUserService service = getMyService(username);
        if(service!= null){
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
        SubscribedUserService service = getMyService(username);
        if(service!= null){
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
        SubscribedUserService service = getMyService(requesting);
        if(service != null){
            Result removed = service.removeAdmin(shopID,toRemove);
            return removed.isOk();
        }
        return false;
    }

    @Override
    public boolean reOpenShop(String username, int shopID){
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Result reopened = service.reopenShop(shopID);
            return reopened.isOk();
        }
        return false;
    }

    @Override
    public Integer createProductByQuantityDiscount(String username, int productId, int productQuantity, double discount, int connectId, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Integer> discountID = service.createProductByQuantityDiscount(productId,productQuantity,discount,connectId,shopId);
            if(discountID.isOk())
                return discountID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createProductDiscount(String username, int productId, double discount, int connectId, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Integer> discountID = service.createProductDiscount(productId,discount,connectId,shopId);
            if(discountID.isOk())
                return discountID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createProductQuantityInPriceDiscount(String username, int productID, int quantity, double priceForQuantity, int connectId, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Integer> discountID = service.createProductQuantityInPriceDiscount(productID,quantity,priceForQuantity,connectId,shopId);
            if(discountID.isOk())
                return discountID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createRelatedGroupDiscount(String username, String category, double discount, int connectId, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Integer> discountID = service.createRelatedGroupDiscount(category,discount,connectId,shopId);
            if(discountID.isOk())
                return discountID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createShopDiscount(String username, int basketQuantity, double discount, int connectId, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Integer> discountID = service.createShopDiscount(basketQuantity,discount,connectId,shopId);
            if(discountID.isOk())
                return discountID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createDiscountAndPolicy(String username,DiscountPred discountPred, DiscountRules discountPolicy, int connectId, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Integer> policyID = service.createDiscountAndPolicy(discountPred,discountPolicy,connectId,shopId);
            if(policyID.isOk())
                return policyID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createDiscountMaxPolicy(String username,DiscountRules discountPolicy, int connectId, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Integer> policyID = service.createDiscountMaxPolicy(discountPolicy,connectId,shopId);
            if(policyID.isOk())
                return policyID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createDiscountOrPolicy(String username,DiscountPred discountPred, DiscountRules discountPolicy, int connectId, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Integer> policyID = service.createDiscountOrPolicy(discountPred,discountPolicy,connectId,shopId);
            if(policyID.isOk())
                return policyID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createDiscountPlusPolicy(String username,DiscountRules discountPolicy, int connectId, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Integer> policyID = service.createDiscountPlusPolicy(discountPolicy,connectId,shopId);
            if(policyID.isOk())
                return policyID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createDiscountXorPolicy(String username,DiscountRules discountRules1, DiscountRules discountRules2, DiscountPred tieBreaker, int connectId, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Integer> policyID = service.createDiscountXorPolicy(discountRules1,discountRules2,tieBreaker,connectId,shopId);
            if(policyID.isOk())
                return policyID.getElement();
        }
        return -1;
    }


    @Override
    public Integer createValidateBasketQuantityDiscount(String username, int basketquantity, boolean cantBeMore, int connectId, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Integer> predID = service.createValidateBasketQuantityDiscount(basketquantity,cantBeMore,connectId,shopId);
            if(predID.isOk())
                return predID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createValidateBasketValueDiscount(String username, double basketvalue, boolean cantBeMore, int connectId, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Integer> predID = service.createValidateBasketValueDiscount(basketvalue,cantBeMore,connectId,shopId);
            if(predID.isOk())
                return predID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createValidateProductQuantityDiscount(String username, int productId, int productQuantity, boolean cantbemore, int connectId, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Integer> predID = service.createValidateProductQuantityDiscount(productId,productQuantity,cantbemore,connectId,shopId);
            if(predID.isOk())
                return predID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createValidateProductPurchase(String username, int productId, int productQuantity, boolean cantbemore, int connectId, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Integer> policyID = service.createValidateProductPurchase(productId,productQuantity,cantbemore,connectId,shopId);
            if(policyID.isOk())
                return policyID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createValidateTImeStampPurchase(String username, LocalTime localTime, boolean buybefore, int conncectId, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Integer> policyID = service.createValidateTImeStampPurchase(localTime,buybefore,conncectId,shopId);
            if(policyID.isOk())
                return policyID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createValidateCategoryPurchase(String username,String category, int productQuantity, boolean cantbemore, int connectId, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Integer> policyID = service.createValidateCategoryPurchase(category,productQuantity,cantbemore,connectId,shopId);
            if(policyID.isOk())
                return policyID.getElement();
        }
        return -1;
    }

    @Override
    public Integer createValidateUserPurchase(String username, int age, int connectId, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Integer> policyID = service.createValidateUserPurchase(age,connectId,shopId);
            if(policyID.isOk())
                return policyID.getElement();
        }
        return -1;
    }

    @Override
    public boolean removePurchasePolicy(String username, int purchasePolicyToDelete, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Boolean> removed = service.removePurchasePolicy(purchasePolicyToDelete,shopId);
            return removed.isOk();
        }
        return false;
    }

    @Override
    public boolean removeDiscount(String username,int discountID, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Boolean> removed = service.removeDiscount(discountID,shopId);
            if(removed.isOk())
                return removed.getElement();
        }
        return false;
    }

    @Override
    public boolean removePredicate(String username,int predicateID, int shopId) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Response<Boolean> removed = service.removePredicate(predicateID,shopId);
            if(removed.isOk())
                return removed.getElement();
        }
        return false;
    }

    @Override
    public boolean setCategory(String username, int productId, String category, int shopID) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Result set = service.setCategory(productId,category,shopID);
            return set.isOk();
        }
        return false;
    }

    @Override
    public boolean approveHeskemMinui(String username,int shop, String adminToAssign) {
        SubscribedUserService service = getMyService(username);
        if(service!= null){
            Result approved = service.approveHeskemMinui(shop, adminToAssign);
            return approved.isOk();
        }
        return false;
    }

    @Override
    public boolean declineHeskemMinui(String username,int shop, String adminToAssign) {
        SubscribedUserService service = getMyService(username);
        if(service != null){
            Result approved = service.declineHeskemMinui(shop, adminToAssign);
            return approved.isOk();
        }
        return false;
    }

    @Override
    public SystemManagerService manageSystemAsSystemManager(String username) {
        SubscribedUserService service = getMyService(username);
        if(service != null){
            Response<SystemManagerService> managerService = service.manageSystemAsSystemManager();
            if(managerService.isOk()) {
                subscribedUsers.remove(username);
                managers.put(username,managerService.getElement());
                return managerService.getElement();
            }
            return null;
        }
        return null;
    }

    public SubscribedUserService getMyService(String username){
        if(managers.containsKey(username))
            return managers.get(username);
        if(subscribedUsers.containsKey(username))
            return subscribedUsers.get(username);
        return null;
    }
}
