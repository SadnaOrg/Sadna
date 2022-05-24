package BusinessLayer.System;

import BusinessLayer.Notifications.Notifier;
import BusinessLayer.Shops.PurchaseHistoryController;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.UserController;

import java.util.concurrent.ConcurrentHashMap;

public class System {
    private Notifier notifier = new Notifier();
    private ExternalServicesSystem externSystem = new ExternalServicesSystem();
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
                paymentSituation.put(shopId, getExternSystem().pay(totalPrices.get(shopId), method));
            }
            else
            {
                paymentSituation.put(shopId,false);
            }
        }
        return paymentSituation;
    }

    public ConcurrentHashMap<Integer, Boolean> checkSupply(ConcurrentHashMap<Integer,PackageInfo> packages){
        ConcurrentHashMap<Integer, Boolean> supplySituation= new ConcurrentHashMap<>();
        for(Integer idx : packages.keySet())
        {
            supplySituation.put(idx, getExternSystem().checkSupply(packages.get(idx)));
        }
        return supplySituation;
    }

    public void notifyUser(String username){
        notifier.notifyUser(username);
    }

    public PurchaseHistoryController getPurchaseHistoryServices() {
        return purchaseHistoryServices;
    }

    public ExternalServicesSystem getExternSystem() {
        return externSystem;
    }

    public synchronized void addPayment(Payment p){
        externSystem.addPayment(p);
    }

    public synchronized void addSupply(Supply s){
        externSystem.addSupply(s);
    }

    public int getPaymentSize(){//for tests only
        return externSystem.getPaymentSize();
    }

    public int getSupplySize(){//for tests only
        return externSystem.getSupplySize();
    }
}
