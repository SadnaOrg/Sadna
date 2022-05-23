package ServiceLayer.interfaces;

import ServiceLayer.Objects.PurchaseHistoryInfo;
import ServiceLayer.Response;
import ServiceLayer.Result;

public interface SystemManagerService extends  SubscribedUserService{
    Response<PurchaseHistoryInfo> getShopsAndUsersInfo(int shop, String userName);

    Response<PurchaseHistoryInfo> getShopsAndUsersInfo(String userName);

    Response<PurchaseHistoryInfo> getShopsAndUsersInfo(int shop);

    Response<PurchaseHistoryInfo> getShopsAndUsersInfo();

    Result removeSubscribedUserFromSystem(String userToRemove);
}
