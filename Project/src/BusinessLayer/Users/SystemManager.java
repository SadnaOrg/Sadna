package BusinessLayer.Users;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.PurchaseHistory;
import ServiceLayer.SystemServiceImp;

import java.util.Collection;

public class SystemManager extends SubscribedUser {

    private final SystemServiceImp systemServiceImp;

    public SystemManager(String userName) {
        super(userName);
        systemServiceImp= new SystemServiceImp();
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo()
    {
        return this.systemServiceImp.getShopsAndUsersInfo();
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo(int shopid)
    {
        return this.systemServiceImp.getShopsAndUsersInfo(shopid);
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo(String userName)
    {
        return this.systemServiceImp.getShopsAndUsersInfo(userName);
    }

    public Collection<PurchaseHistory> getShopsAndUsersInfo(int shopid, String userName)
    {
        return this.systemServiceImp.getShopsAndUsersInfo(shopid, userName);
    }
}
