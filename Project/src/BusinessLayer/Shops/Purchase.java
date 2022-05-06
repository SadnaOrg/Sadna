package BusinessLayer.Shops;

import BusinessLayer.Users.Basket;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class Purchase {
    int transectionid;
    ConcurrentHashMap<Integer , Integer> infoProducts;
    Date dateOfPurchase;
    int shopid;
    String user;

    public Purchase(int shopid, String user, int transactionid, Basket info)
    {
        this.transectionid= transactionid;
        this.infoProducts =new ConcurrentHashMap<>();
        for (int productid: info.getProducts().keySet())
        {
            this.infoProducts.put(productid,info.getProducts().get(productid));
        }
        this.dateOfPurchase= new Date();
        this.user= user;
        this.shopid= shopid;
    }

    public int getTransectionid() {
        return transectionid;
    }

    public int getShopid() {
        return shopid;
    }

    public String getUser() {
        return user;
    }
}
