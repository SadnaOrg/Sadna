package ServiceLayer;

import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.ShopController;
import BusinessLayer.Users.SystemManager;
import ServiceLayer.interfaces.SystemService;
import BusinessLayer.System.System;

import java.util.Collection;

public class SystemServiceImp implements SystemService {

    System system = System.getInstance();

    public Collection<PurchaseHistory> getShopsAndUsersInfo()
    {
        return ShopController.getInstance().getPurchaseInfo();
    }
    public Collection<PurchaseHistory> getShopsAndUsersInfo(int shopid)
    {
        return ShopController.getInstance().getPurchaseInfo(shopid);
    }
    public Collection<PurchaseHistory> getShopsAndUsersInfo(String userName)
    {
        return ShopController.getInstance().getPurchaseInfo(userName);
    }
    public Collection<PurchaseHistory> getShopsAndUsersInfo(int shopid, String userName)
    {
        return ShopController.getInstance().getPurchaseInfo(shopid, userName);
    }

}
