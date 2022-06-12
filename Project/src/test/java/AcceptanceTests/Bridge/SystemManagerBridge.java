package AcceptanceTests.Bridge;


import AcceptanceTests.DataObjects.PurchaseHistoryInfo;
import AcceptanceTests.DataObjects.SubscribedUserInfo;

import java.util.List;

public interface SystemManagerBridge {
    PurchaseHistoryInfo getShopsAndUsersInfo(int shop, String userName);

    PurchaseHistoryInfo getShopsAndUsersInfo(String userName);

    PurchaseHistoryInfo getShopsAndUsersInfo(int shop);

    PurchaseHistoryInfo getShopsAndUsersInfo();

    boolean removeSubscribedUserFromSystem(String userToRemove);

    List<SubscribedUserInfo> getAllSubscribedUserInfo();
}
