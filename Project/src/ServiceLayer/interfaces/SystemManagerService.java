package ServiceLayer.interfaces;

import BusinessLayer.Shops.PurchaseHistory;
import ServiceLayer.Response;

import java.util.Collection;

public interface SystemManagerService {
    Response<Collection<PurchaseHistory>> getShopsAndUsersInfo(int shop, String userName);

    Response<Collection<PurchaseHistory>> getShopsAndUsersInfo(String userName);

    Response<Collection<PurchaseHistory>> getShopsAndUsersInfo(int shop);

    Response<Collection<PurchaseHistory>> getShopsAndUsersInfo();
}
