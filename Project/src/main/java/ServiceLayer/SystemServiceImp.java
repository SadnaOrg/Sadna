package main.java.ServiceLayer;

import main.java.BusinessLayer.Shops.PurchaseHistory;
import main.java.BusinessLayer.Shops.PurchaseHistoryController;
import main.java.BusinessLayer.Shops.ShopController;
import main.java.BusinessLayer.Users.SystemManager;
import main.java.ServiceLayer.interfaces.SystemService;
import main.java.BusinessLayer.System.System;

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
