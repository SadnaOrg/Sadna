package ServiceLayer;

import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.PurchaseHistoryController;
import BusinessLayer.Shops.ShopController;
import BusinessLayer.Users.SystemManager;
import ServiceLayer.interfaces.SystemService;
import BusinessLayer.System.System;

import java.util.Collection;

public class SystemServiceImp {

    System system = System.getInstance();

    public Collection<PurchaseHistory> getShopsAndUsersInfo()
    {
        return PurchaseHistoryController.getInstance().getPurchaseInfo();
    }
    public Collection<PurchaseHistory> getShopsAndUsersInfo(int shopid)
    {
        return PurchaseHistoryController.getInstance().getPurchaseInfo(shopid);
    }
    public Collection<PurchaseHistory> getShopsAndUsersInfo(String userName)
    {
        return PurchaseHistoryController.getInstance().getPurchaseInfo(userName);
    }
    public Collection<PurchaseHistory> getShopsAndUsersInfo(int shopid, String userName)
    {
        return PurchaseHistoryController.getInstance().getPurchaseInfo(shopid, userName);
    }

}
