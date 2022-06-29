package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.PurchaseHistoryInfo;
import AcceptanceTests.DataObjects.SubscribedUserInfo;

import java.util.List;

public class SystemManagerProxy extends SubscribedUserProxy implements SystemManagerBridge{
    private final SystemManagerAdapter managerAdapter;

    public SystemManagerProxy(SubscribedUserProxy proxy){
        super(new SystemManagerAdapter(proxy.getGuests(),proxy.getSubscribed(), proxy.getNotifications()));
        managerAdapter = (SystemManagerAdapter) super.subscribedUserAdapter;
    }

    @Override
    public PurchaseHistoryInfo getShopsAndUsersInfo(String username, int shop, String userName) {
        return managerAdapter.getShopsAndUsersInfo(username,shop,userName);
    }

    @Override
    public PurchaseHistoryInfo getShopsAndUsersInfo(String username, String u) {
        return managerAdapter.getShopsAndUsersInfo(username,u);
    }

    @Override
    public PurchaseHistoryInfo getShopsAndUsersInfo(String username, int shop) {
        return managerAdapter.getShopsAndUsersInfo(username,shop);
    }

    @Override
    public PurchaseHistoryInfo getShopsAndUsersInfo(String username) {
        return managerAdapter.getShopsAndUsersInfo(username);
    }

    @Override
    public boolean removeSubscribedUserFromSystem(String username, String userToRemove) {
        return managerAdapter.removeSubscribedUserFromSystem(username,userToRemove);
    }

    @Override
    public List<SubscribedUserInfo> getAllSubscribedUserInfo(String username) {
        return managerAdapter.getAllSubscribedUserInfo(username);
    }



}
