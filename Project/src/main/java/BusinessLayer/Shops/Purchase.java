package BusinessLayer.Shops;

import BusinessLayer.Products.ProductInfo;
import BusinessLayer.Users.Basket;


import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

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

    public Purchase(int transectionid, Collection<ProductInfo> productInfos, Date dateOfPurchase, int shopid, String user) {
        this.transectionid = transectionid;
        this.infoProducts = new ConcurrentHashMap<>();
        this.productPrices = new ConcurrentHashMap<>();
        for (ProductInfo productInfo:productInfos)
        {
            this.infoProducts.put(productInfo.getProductid(),productInfo.getProductquantity());
            this.productPrices.put(productInfo.getProductid(),productInfo.getProductprice());
        }
        this.dateOfPurchase = dateOfPurchase;
        this.shopid = shopid;
        this.user = user;
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
