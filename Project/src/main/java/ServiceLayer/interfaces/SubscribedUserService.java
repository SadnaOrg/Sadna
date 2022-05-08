package main.java.ServiceLayer.interfaces;

import main.java.BusinessLayer.Shops.PurchaseHistory;
import main.java.BusinessLayer.Users.AdministratorInfo;
import main.java.BusinessLayer.Users.BaseActions.BaseActionType;
import main.java.ServiceLayer.Response;
import main.java.ServiceLayer.Result;

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
