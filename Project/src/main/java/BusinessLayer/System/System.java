package BusinessLayer.System;

import BusinessLayer.Shops.PurchaseHistoryController;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.UserController;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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
        UserController.getInstance().createSystemManager("Admin","ILoveIttaiNeria");
    }
    public ConcurrentHashMap<Integer,Boolean> pay(ConcurrentHashMap<Integer,Double> totalPrices, PaymentMethod method)
    {
        ConcurrentHashMap<Integer,Boolean> paymentSituation= new ConcurrentHashMap<>();
        for(int shopId: totalPrices.keySet())
        {
            if(totalPrices.get(shopId)>0 && method != null) {
                paymentSituation.put(shopId, externSystem.pay(totalPrices.get(shopId), method));
            }
            else
            {
                paymentSituation.put(shopId,false);
            }
        }
        return paymentSituation;
    }

    public ConcurrentHashMap<AtomicInteger, Boolean> checkSupply(ConcurrentHashMap<AtomicInteger,PackageInfo> packages){
        ConcurrentHashMap<AtomicInteger, Boolean> supplySituation= new ConcurrentHashMap<>();
        for(AtomicInteger idx : packages.keySet())
        {
            supplySituation.put(idx, externSystem.checkSupply(packages.get(idx)));
        }
        return supplySituation;
    }

    public PurchaseHistoryController getPurchaseHistoryServices() {
        return purchaseHistoryServices;
    }
}
