package main.java.BusinessLayer.Users;

import main.java.BusinessLayer.Shops.PurchaseHistory;
import main.java.BusinessLayer.Shops.PurchaseHistoryController;

import java.util.Collection;

public class SystemManager extends SubscribedUser {


    public SystemManager(String userName,String password) {
        super(userName,password);
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo()
    {
        return PurchaseHistoryController.getInstance().getPurchaseInfo();
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo(int shopId)
    {
        return PurchaseHistoryController.getInstance().getPurchaseInfo(shopId);
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo(String userName)
    {
        return PurchaseHistoryController.getInstance().getPurchaseInfo(userName);
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo(int shopId, String userName)
    {
        return PurchaseHistoryController.getInstance().getPurchaseInfo(shopId, userName);
    }
}
