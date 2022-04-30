package BusinessLayer.System;

import BusinessLayer.Shops.PurchaseHistoryController;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.UserController;

import java.util.concurrent.ConcurrentHashMap;

public class System {
    private Notifier notifier;
    private ExternalServicesSystem externSystem;
    private PurchaseHistoryController purchaseHistoryServices;
    private ConcurrentHashMap<Integer, Shop> shops;

    static private class SystemHolder{
        static final System s = new System();
    }
    public static System getInstance()
    {
        return SystemHolder.s;
    }

    public void initialize(){
        notifier = new Notifier();
        externSystem = new ExternalServicesSystem();
        shops = new ConcurrentHashMap<>();
        purchaseHistoryServices= PurchaseHistoryController.getInstance();
        UserController.getInstance().createSystemManager("Admin");
    }
    public ConcurrentHashMap<Integer,Boolean> pay(ConcurrentHashMap<Integer,Double> totalPrices)
    {
        ConcurrentHashMap<Integer,Boolean> paymensituation= new ConcurrentHashMap<>();
        for(int shopid: totalPrices.keySet())
        {
            if(totalPrices.get(shopid)>0) {
                paymensituation.put(shopid, externSystem.pay(totalPrices.get(shopid)));
            }
            else
            {
                paymensituation.put(shopid,false);
            }
        }
        return paymensituation;
    }

    public PurchaseHistoryController getPurchaseHistoryServices() {
        return purchaseHistoryServices;
    }
}
