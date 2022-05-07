package BusinessLayer.Shops;


import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductInfo;

import java.util.concurrent.ConcurrentHashMap;

public class ShopInfo {
    private int shopid;
    private String shopname;
    private String shopdescription;
    private ConcurrentHashMap<Integer, ProductInfo> shopproductsinfo;

    public ShopInfo(Shop s)
    {
        this.shopid = s.getId();
        this.shopname= s.getName();
        this.shopdescription= s.getDescription();
        shopproductsinfo = new ConcurrentHashMap<>();
        for (Product p : s.getProducts().values())
        {
            ProductInfo productInfo = new ProductInfo(p);
            shopproductsinfo.put(productInfo.getProductid(),productInfo);
        }
    }


    public ShopInfo getInfo()
    {
        return this;
    }

}