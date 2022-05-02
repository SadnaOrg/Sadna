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
        Collection<PurchaseHistory> ph = PurchaseHistoryController.getInstance().getPurchaseInfo();
        Log.getInstance().event("get purchase info succeeded");
        return ph;
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo(int shopid)
    {
        Collection<PurchaseHistory> ph = PurchaseHistoryController.getInstance().getPurchaseInfo(shopid);
        Log.getInstance().event("get purchase info succeeded");
        return ph;
    }
    public Collection<PurchaseHistory> getShopsAndUsersInfo(String userName)
    {
        Collection<PurchaseHistory> ph =  PurchaseHistoryController.getInstance().getPurchaseInfo(userName);
        Log.getInstance().event("get purchase info succeeded");
        return ph;
    }
    public Collection<PurchaseHistory> getShopsAndUsersInfo(int shopid, String userName)
    {
        Collection<PurchaseHistory> ph =  PurchaseHistoryController.getInstance().getPurchaseInfo(shopid, userName);
        Log.getInstance().event("get purchase info succeeded");
        return ph;
    }
}
