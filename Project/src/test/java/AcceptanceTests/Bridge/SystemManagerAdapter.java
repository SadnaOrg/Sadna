package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;
import ServiceLayer.Response;
import ServiceLayer.Result;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.SystemManagerService;
import ServiceLayer.interfaces.UserService;

import java.util.HashMap;
import java.util.List;

public class SystemManagerAdapter extends SubscribedUserAdapter implements SystemManagerBridge{
    public SystemManagerAdapter(HashMap<String, UserService> guests, HashMap<String, SubscribedUserService> subscribed, HashMap<String, List<Notification>> notifications) {
        super(guests, subscribed,notifications);
    }

    @Override
    public PurchaseHistoryInfo getShopsAndUsersInfo(String manager,int shop, String userName) {
        if(managers.containsKey(manager)){
            SystemManagerService service = managers.get(manager);
            Response<ServiceLayer.Objects.PurchaseHistoryInfo> purchaseHistoryInfoResponse = service.getShopsAndUsersInfo(shop,userName);
            if(purchaseHistoryInfoResponse.isOk())
                return new PurchaseHistoryInfo(purchaseHistoryInfoResponse.getElement());
            return null;
        }
        return null;
    }

    @Override
    public PurchaseHistoryInfo getShopsAndUsersInfo(String manager,String userName) {
        if(managers.containsKey(manager)){
            SystemManagerService service = managers.get(manager);
            Response<ServiceLayer.Objects.PurchaseHistoryInfo> purchaseHistoryInfoResponse = service.getShopsAndUsersInfo(userName);
            if(purchaseHistoryInfoResponse.isOk())
                return new PurchaseHistoryInfo(purchaseHistoryInfoResponse.getElement());
            return null;
        }
        return null;
    }
    @Override
    public AcceptanceTests.DataObjects.SubscribedUser login(String guestName, RegistrationInfo info) {
        SubscribedUser res = super.login(guestName,info);
        if(res == null)
            return null;
        SubscribedUserService service = super.getMyService(res.name);
        this.subscribedUsers.put(guestName, service);
        return res;
    }

    @Override
    public PurchaseHistoryInfo getShopsAndUsersInfo(String manager,int shop) {
        if(managers.containsKey(manager)){
            SystemManagerService service = managers.get(manager);
            Response<ServiceLayer.Objects.PurchaseHistoryInfo> purchaseHistoryInfoResponse = service.getShopsAndUsersInfo(shop);
            if(purchaseHistoryInfoResponse.isOk())
                return new PurchaseHistoryInfo(purchaseHistoryInfoResponse.getElement());
            return null;
        }
        return null;
    }

    @Override
    public PurchaseHistoryInfo getShopsAndUsersInfo(String manager) {
        if(managers.containsKey(manager)){
            SystemManagerService service = managers.get(manager);
            Response<ServiceLayer.Objects.PurchaseHistoryInfo> purchaseHistoryInfoResponse = service.getShopsAndUsersInfo();
            if(purchaseHistoryInfoResponse.isOk())
                return new PurchaseHistoryInfo(purchaseHistoryInfoResponse.getElement());
            return null;
        }
        return null;
    }

    @Override
    public boolean removeSubscribedUserFromSystem(String manager,String userToRemove) {
        if(managers.containsKey(manager)){
            SystemManagerService service = managers.get(manager);
            Result purchaseHistoryInfoResponse = service.removeSubscribedUserFromSystem(userToRemove);
            return purchaseHistoryInfoResponse.isOk();
        }
        return false;
    }

    @Override
    public List<SubscribedUserInfo> getAllSubscribedUserInfo(String manager) {
        if(managers.containsKey(manager)){
            SystemManagerService service = managers.get(manager);
            Response<List<ServiceLayer.Objects.SubscribedUserInfo>> r = service.getAllSubscribedUserInfo();
            if(r.isOk())
                return r.getElement().stream().map(SubscribedUserInfo::new).toList();
            return null;
        }
        return null;
    }

}
