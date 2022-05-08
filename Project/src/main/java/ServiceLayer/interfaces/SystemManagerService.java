package main.java.ServiceLayer.interfaces;

import main.java.BusinessLayer.Shops.PurchaseHistory;
import main.java.ServiceLayer.Response;

import java.util.Collection;

public interface SystemManagerService {
    Response<Collection<PurchaseHistory>> getShopsAndUsersInfo(int shop, String userName);

    Response<Collection<PurchaseHistory>> getShopsAndUsersInfo(String userName);

    Response<Collection<PurchaseHistory>> getShopsAndUsersInfo(int shop);

    Response<Collection<PurchaseHistory>> getShopsAndUsersInfo();
}
