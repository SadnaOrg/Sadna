package AcceptanceTests.Bridge;


import AcceptanceTests.DataObjects.PurchaseHistoryInfo;
import AcceptanceTests.DataObjects.SubscribedUserInfo;
import ServiceLayer.interfaces.SystemManagerService;

import java.util.List;

public interface SystemManagerBridge {
    PurchaseHistoryInfo getShopsAndUsersInfo(String username, int shop, String userName);

    PurchaseHistoryInfo getShopsAndUsersInfo(String username,String u);

    PurchaseHistoryInfo getShopsAndUsersInfo(String username,int shop);

    PurchaseHistoryInfo getShopsAndUsersInfo(String username);

    boolean removeSubscribedUserFromSystem(String username,String userToRemove);

    List<SubscribedUserInfo> getAllSubscribedUserInfo(String username);
}
