package BusinessLayer.Users;

import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.PurchaseHistoryController;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SystemManager extends SubscribedUser {


    public SystemManager(String userName,String password) {
        super(userName,password);
    }

    public SystemManager(String username, boolean isNotRemoved, String hashedPassword, List<ShopAdministrator> shopAdministrator, boolean is_login) {
        super(username, isNotRemoved, hashedPassword, shopAdministrator, is_login);
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

    public boolean removeSubscribedUser(String userName){
        return UserController.getInstance().removeSubscribedUserFromSystem(userName);
    }


}
