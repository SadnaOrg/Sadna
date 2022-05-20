package BusinessLayer.Shops;

import BusinessLayer.Products.Users.Basket;


import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.DoubleBinaryOperator;

public class Purchase {
    int transectionid;
    ConcurrentHashMap<Integer , Integer> infoProducts;
    ConcurrentHashMap<Integer,Double> productPrices;
    Date dateOfPurchase;
    int shopid;
    String user;

    public Purchase(int shopid, String user, int transactionid, Basket info)
    {
        this.transectionid= transactionid;
        this.infoProducts =new ConcurrentHashMap<>();
        this.productPrices = new ConcurrentHashMap<>();
        for (int productid: info.getProducts().keySet())
        {
            this.infoProducts.put(productid,info.getProducts().get(productid));
            this.productPrices.put(productid,info.getPrices().get(productid));
        }
        this.dateOfPurchase= new Date();
        this.user= user;
        this.shopid= shopid;
    }

    public int getTransectionid() {
        return transectionid;
    }

    public ConcurrentHashMap<Integer, Integer> getInfoProducts() {
        return infoProducts;
    }

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }

    public int getShopid() {
        return shopid;
    }

    public String getUser() {
        return user;
    }

    public ConcurrentHashMap<Integer, Double> getProductPrices(){return this.productPrices;}
}
