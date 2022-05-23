package ServiceLayer.interfaces;

import ServiceLayer.Objects.PurchaseHistoryInfo;
import ServiceLayer.Objects.SubscribedUserInfo;
import ServiceLayer.Response;
import ServiceLayer.Result;

import java.util.List;

public interface SystemManagerService extends  SubscribedUserService{
    Response<PurchaseHistoryInfo> getShopsAndUsersInfo(int shop, String userName);

    Response<PurchaseHistoryInfo> getShopsAndUsersInfo(String userName);

    Response<PurchaseHistoryInfo> getShopsAndUsersInfo(int shop);

    Response<PurchaseHistoryInfo> getShopsAndUsersInfo();

    Result removeSubscribedUserFromSystem(String userToRemove);

    Response<List<SubscribedUserInfo>> getAllSubscribedUserInfo();

}
