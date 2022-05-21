package BusinessLayer.Products.Users;

import java.util.concurrent.ConcurrentHashMap;

public class BasketInfo {
    private int shopid;
    private ConcurrentHashMap<Integer , Integer> products;
    private ConcurrentHashMap<Integer, Double> prices;

    public BasketInfo(Basket basket)
    {
        this.shopid= basket.getShopid();
        this.products = new ConcurrentHashMap<>();
        this.prices = new ConcurrentHashMap<>();
        for (int productid: basket.getProducts().keySet())
        {
            this.products.put(productid,basket.getProducts().get(productid));
            this.prices.put(productid,basket.getPrices().get(productid));
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

    public ConcurrentHashMap<Integer,Double> getPrices(){
        return this.prices;
    }
}
