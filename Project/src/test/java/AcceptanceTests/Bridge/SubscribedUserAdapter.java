package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;
import ServiceLayer.BaseActionType;
import ServiceLayer.Objects.Administrator;
import ServiceLayer.Objects.AdministratorInfo;
import ServiceLayer.Objects.User;
import ServiceLayer.Response;
import ServiceLayer.Result;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.UserService;

import java.util.*;

public class SubscribedUserAdapter extends UserAdapter implements SubscribedUserBridge{

    public void setUsers(HashMap<String,UserService> guests, HashMap<String,SubscribedUserService> subscribed){
        users = guests;
        subscribedUsers = subscribed;
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
        if(subscribedUsers.containsKey(appointeeName)){
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
    public boolean addManagerPermission(int shopID, String giverName, String receiverName, SubscribedUser.Permission permission) {
        return addAdminPermission(shopID,giverName,receiverName,permission);
    }

    @Override
    public boolean addOwnerPermission(int shopID, String giverName, String receiverName, SubscribedUser.Permission permission) {
        return addAdminPermission(shopID,giverName,receiverName,permission);
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
            Result added = service.addProductToShop(shopID,desc,name,manufacturer,productID,quantity,price);
            return added.isOk();
        }
        return false;
    }

    @Override
    public Shop openShop(String username, String name, String desc) {
        if(subscribedUsers.containsKey(username)){
            SubscribedUserService service = subscribedUsers.get(username);
            Response<ServiceLayer.Objects.Shop> newShop = service.openShop(name,desc);
            if(newShop.isOk())
                return new Shop(newShop.getElement());
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
    public boolean removePermission(int shopID, String removing, String removeTo, SubscribedUser.Permission permission) {
        return removeAdminPermission(shopID,removeTo,removeTo,permission);
    }

    private boolean addAdminPermission(int shopID, String giverName, String receiverName, SubscribedUser.Permission permission){
        if(subscribedUsers.containsKey(giverName)){
            SubscribedUserService service = subscribedUsers.get(giverName);
            Response<Administrator> info = service.getMyInfo(shopID);
            if(info.isOk()){
                Collection<ServiceLayer.BaseActionType> permissions = info.getElement().getPermissions();
                Collection<Integer> actions = new LinkedList<>();
                for (ServiceLayer.BaseActionType action:
                        permissions) {
                    actions.add(action.getCode());
                }
                actions.add(permission.getCode());
                Result added = service.changeManagerPermission(shopID,receiverName, actions);
                return added.isOk();
            }
            return false;
        }
        return false;
    }

    private boolean removeAdminPermission(int shopID, String giverName, String receiverName, SubscribedUser.Permission permission){
        if(subscribedUsers.containsKey(giverName)){
            SubscribedUserService service = subscribedUsers.get(giverName);
            Response<Administrator> info = service.getMyInfo(shopID);
            if(info.isOk()){
                Collection<ServiceLayer.BaseActionType> permissions = info.getElement().getPermissions();
                Collection<Integer> actions = new LinkedList<>();
                for (ServiceLayer.BaseActionType action:
                        permissions) {
                    actions.add(action.getCode());
                }
                actions.remove(permission.getCode());
                Result added = service.changeManagerPermission(shopID,receiverName, actions);
                return added.isOk();
            }
            return false;
        }
        return false;
    }

}
