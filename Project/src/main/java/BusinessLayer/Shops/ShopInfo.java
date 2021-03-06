package BusinessLayer.Shops;


import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductInfo;

import java.util.concurrent.ConcurrentHashMap;

public class ShopInfo {
    private int shopid;
    private String shopname;
    private String shopdescription;
    private ConcurrentHashMap<Integer, ProductInfo> shopproductsinfo;
    private boolean isOpen;

    public ShopInfo(Shop s)
    {
        this.shopid = s.getId();
        this.shopname= s.getName();
        this.shopdescription= s.getDescription();
        this.isOpen = s.isOpen();
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

    public int getShopid() {
        return shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public String getShopdescription() {
        return shopdescription;
    }

    public ConcurrentHashMap<Integer, ProductInfo> getShopproductsinfo() {
        return shopproductsinfo;
    }

    public boolean isOpen(){
        return isOpen;
    }
}
