package BusinessLayer.Shops;

import BusinessLayer.System.System;
import BusinessLayer.Users.UserController;

import java.util.ArrayList;
import java.util.Collection;

public class PurchaseHistoryController {


    private Collection<PurchaseHistory> DataOnPurchases;

    static private class PurchaseHistoryControllerHolder {
        static final BusinessLayer.Shops.PurchaseHistoryController ph = new BusinessLayer.Shops.PurchaseHistoryController();
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
            if(purchaseHistory.getShopid()== shopid) {
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
            if(purchaseHistory.getUser().equals(user) && purchaseHistory.getShopid()== shopid) {
                relevantinfo.add(purchaseHistory);
            }
        }
        return relevantinfo;
    }

    public PurchaseHistory createPurchaseHistory(int shopid, String user)
    {
        PurchaseHistory purchaseHistory= new PurchaseHistory(shopid , user);
        DataOnPurchases.add(purchaseHistory);
        return purchaseHistory;
    }
}
