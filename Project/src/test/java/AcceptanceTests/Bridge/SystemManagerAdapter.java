package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.PurchaseHistoryInfo;
import AcceptanceTests.DataObjects.SubscribedUserInfo;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.UserService;

import java.util.HashMap;
import java.util.List;

public class SystemManagerAdapter extends SubscribedUserAdapter implements SystemManagerBridge{
    public SystemManagerAdapter(HashMap<String, UserService> guests, HashMap<String, SubscribedUserService> subscribed) {
        super(guests, subscribed);
    }

    @Override
    public PurchaseHistoryInfo getShopsAndUsersInfo(int shop, String userName) {
        return null;
    }

    @Override
    public PurchaseHistoryInfo getShopsAndUsersInfo(String userName) {
        return null;
    }

    @Override
    public PurchaseHistoryInfo getShopsAndUsersInfo(int shop) {
        return null;
    }

    @Override
    public PurchaseHistoryInfo getShopsAndUsersInfo() {
        return null;
    }

    @Override
    public boolean removeSubscribedUserFromSystem(String userToRemove) {
        return false;
    }

    @Override
    public List<SubscribedUserInfo> getAllSubscribedUserInfo() {
        return null;
    }
}
