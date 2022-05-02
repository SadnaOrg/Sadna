package ServiceLayer;

import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.PurchaseHistoryController;
import BusinessLayer.Shops.ShopController;
import BusinessLayer.Users.SystemManager;
import ServiceLayer.interfaces.SystemService;
import BusinessLayer.System.System;

import java.util.Collection;

public class SystemServiceImp implements SystemService {

    System system = System.getInstance();
    private SystemManager currManager;

    public SystemServiceImp(SystemManager currManager) {
        setCurrManager(currManager);
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo()
    {
        Collection<PurchaseHistory> ph = currManager.getShopsAndUsersInfo();
        Log.getInstance().event("get purchase info succeeded");
        return ph;
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo(int shopid)
    {
        Collection<PurchaseHistory> ph = currManager.getShopsAndUsersInfo(shopid);
        Log.getInstance().event("get purchase info succeeded");
        return ph;
    }
    public Collection<PurchaseHistory> getShopsAndUsersInfo(String userName)
    {
        Collection<PurchaseHistory> ph =  currManager.getShopsAndUsersInfo(userName);
        Log.getInstance().event("get purchase info succeeded");
        return ph;
    }
    public Collection<PurchaseHistory> getShopsAndUsersInfo(int shopid, String userName)
    {
        Collection<PurchaseHistory> ph =  currManager.getShopsAndUsersInfo(shopid, userName);
        Log.getInstance().event("get purchase info succeeded");
        return ph;
    }

    private void setCurrManager(SystemManager currManager) {
        this.currManager = currManager;
    }
}
