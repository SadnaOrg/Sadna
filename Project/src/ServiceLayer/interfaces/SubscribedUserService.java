package ServiceLayer.interfaces;

import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Users.AdministratorInfo;
import BusinessLayer.Users.BaseActions.BaseActionType;
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

    Response<Collection<AdministratorInfo>> getAdministratorInfo(int shop);

    Response<Collection<PurchaseHistory>> getHistoryInfo(int shop);
}
