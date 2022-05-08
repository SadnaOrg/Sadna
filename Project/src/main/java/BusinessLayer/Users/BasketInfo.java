package main.java.BusinessLayer.Users;

import java.util.concurrent.ConcurrentHashMap;

public class BasketInfo {
    private int shopid;
    private ConcurrentHashMap<Integer , Integer> products;

    public BasketInfo(Basket basket)
    {
        this.shopid= basket.getShopid();
        this.products = new ConcurrentHashMap<>();
        for (int productid: products.keySet())
        {
            this.products.put(productid,basket.getProducts().get(productid));
        }
    }

    public BasketInfo getBasket()
    {
        return this;
    }

    public int getShopid() {
        return shopid;
    }

    public ConcurrentHashMap<Integer, Integer> getProducts() {
        return products;
    }
}
