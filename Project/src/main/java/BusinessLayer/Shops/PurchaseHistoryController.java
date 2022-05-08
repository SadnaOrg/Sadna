package main.java.BusinessLayer.Shops;

import main.java.BusinessLayer.System.System;
import main.java.BusinessLayer.Users.UserController;

import java.util.ArrayList;
import java.util.Collection;

public class PurchaseHistoryController {


    private Collection<PurchaseHistory> DataOnPurchases;

    static private class PurchaseHistoryControllerHolder {
        static final main.java.BusinessLayer.Shops.PurchaseHistoryController ph = new main.java.BusinessLayer.Shops.PurchaseHistoryController();
    }
    private PurchaseHistoryController()
    {
        this.DataOnPurchases = new ArrayList<>();
    }

    public static PurchaseHistoryController getInstance(){
        return PurchaseHistoryController.PurchaseHistoryControllerHolder.ph;
    }

    public Collection<PurchaseHistory> getDataOnPurchases() {
        return DataOnPurchases;
    }

    public Collection<PurchaseHistory> getPurchaseInfo()
    {
        return DataOnPurchases;

    }
    public Collection<PurchaseHistory> getPurchaseInfo(String user)
    {
        Collection<PurchaseHistory> allinfo= DataOnPurchases;
        Collection<PurchaseHistory> relevantinfo= new ArrayList<>();
        for(PurchaseHistory purchaseHistory:allinfo)
        {
            if(purchaseHistory.getUser().equals(user)) {
                relevantinfo.add(purchaseHistory);
            }
        }
        return relevantinfo;
    }

    public Collection<PurchaseHistory> getPurchaseInfo(int shopid)
    {
        Collection<PurchaseHistory> allinfo= DataOnPurchases;
        Collection<PurchaseHistory> relevantinfo= new ArrayList<>();
        for(PurchaseHistory purchaseHistory:allinfo)
        {
            if(purchaseHistory.getShop().getId()== shopid) {
                relevantinfo.add(purchaseHistory);
            }
        }
        return relevantinfo;
    }
    public Collection<PurchaseHistory> getPurchaseInfo(int shopid, String user)
    {
        Collection<PurchaseHistory> allinfo= DataOnPurchases;
        Collection<PurchaseHistory> relevantinfo= new ArrayList<>();
        for(PurchaseHistory purchaseHistory:allinfo)
        {
            if(purchaseHistory.getUser().equals(user) && purchaseHistory.getShop().getId()== shopid) {
                relevantinfo.add(purchaseHistory);
            }
        }
        return relevantinfo;
    }

    public PurchaseHistory createPurchaseHistory(Shop shop, String user)
    {
        PurchaseHistory purchaseHistory= new PurchaseHistory(shop , user);
        DataOnPurchases.add(purchaseHistory);
        return purchaseHistory;
    }
}
