package BusinessLayer.System;

import BusinessLayer.Notifications.Notification;
import BusinessLayer.Notifications.Notifier;
import BusinessLayer.Products.Product;
import BusinessLayer.Shops.PurchaseHistoryController;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Shops.ShopController;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.UserController;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class System {

    private final Notifier notifier;
    private ExternalServicesSystem externSystem = new ExternalServicesSystem();
    private PurchaseHistoryController purchaseHistoryServices;
    private ConcurrentHashMap<Integer, Shop> shops;

    public System() {
        notifier = new Notifier();;
    }

    static private class SystemHolder{
        static final System s = new System();
        static {
            s.initialize();
        }
    }

    public static System getInstance()
    {
        return SystemHolder.s;
    }

    public void initialize(){
        externSystem = new ExternalServicesSystem();
        shops = new ConcurrentHashMap<>();
        purchaseHistoryServices= PurchaseHistoryController.getInstance();
        UserController.getInstance().createSystemManager("Admin","ILoveIttaiNeria",new Date(2001, Calendar.DECEMBER,1));
        //setUp();
    }




    private static PurchaseHistoryController phc = PurchaseHistoryController.getInstance();
    private static ShopController sc = ShopController.getInstance();
    private static Shop s1;
    private static SubscribedUser buyer = new SubscribedUser("buyer", "I am also not Guy Kishon",new Date(2001, Calendar.DECEMBER,1));
    private static final int shopId = 1200;
    private static final int otherShopId = 12930;
    private static Product p1 = new Product(12090, "pord", 156.2, 45);
    private static Basket basket = new Basket(shopId);
    private static UserController uc = UserController.getInstance();
    private static boolean flag = false;
    public static void setUp(){
        if(!flag) {
            uc.registerToSystem(buyer.getUserName(), "I am also not Guy Kishon",new Date(2001, Calendar.DECEMBER,1));
            uc.login(buyer.getUserName(), "I am also not Guy Kishon", buyer);
            s1 = new Shop(shopId, "name of shop","testing shop", buyer);
            s1.addProduct(p1);
            basket.saveProducts(p1.getID(), 23, p1.getPrice(),"meow");
            s1.addBasket(buyer.getUserName(), basket);
            sc.addShop(s1);
            sc.addToPurchaseHistory(buyer.getUserName(), createPayments());
            phc.createPurchaseHistory(s1, buyer.getUserName());
        }
        flag = true;
    }

    public static ConcurrentHashMap<Integer, Boolean> createPayments() {
        ConcurrentHashMap<Integer, Boolean> payments = new ConcurrentHashMap<>();
        payments.put(shopId, true);
        return payments;
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

    public Notifier getNotifier(){
        return notifier;
    }


}
