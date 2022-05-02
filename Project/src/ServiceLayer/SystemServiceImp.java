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
        return currManager.getShopsAndUsersInfo();
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo(int shopid)
    {
        return currManager.getShopsAndUsersInfo(shopid);
    }
    public Collection<PurchaseHistory> getShopsAndUsersInfo(String userName)
    {
        return currManager.getShopsAndUsersInfo(userName);
    }
    public Collection<PurchaseHistory> getShopsAndUsersInfo(int shopid, String userName)
    {
        return currManager.getShopsAndUsersInfo(shopid, userName);
    }

    private void setCurrManager(SystemManager currManager) {
        this.currManager = currManager;
    }
}
