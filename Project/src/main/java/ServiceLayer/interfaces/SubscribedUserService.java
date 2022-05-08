package ServiceLayer.interfaces;

import BusinessLayer.Users.BaseActions.BaseActionType;
import ServiceLayer.Objects.AdministratorInfo;
import ServiceLayer.Objects.PurchaseHistoryInfo;
import ServiceLayer.Response;
import ServiceLayer.Result;

import java.util.Collection;

public interface SubscribedUserService extends UserService {

    Response<SystemManagerService> manageSystemAsSystemManager();

    Result logout();

    Result openShop(String name);


    Result assignShopManager(int shop, String userNameToAssign);

    Result assignShopOwner(int shop, String userNameToAssign);

    Result changeManagerPermission(int shop, String userNameToAssign, Collection<BaseActionType> types);

    Result closeShop(int shop);

    Response<AdministratorInfo> getAdministratorInfo(int shop);

    Response<PurchaseHistoryInfo> getHistoryInfo(int shop);
}
