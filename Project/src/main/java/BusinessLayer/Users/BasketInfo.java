package BusinessLayer.Users;

import java.util.concurrent.ConcurrentHashMap;

public class BasketInfo {
    private int shopid;
    private ConcurrentHashMap<Integer , Integer> products;
    private ConcurrentHashMap<Integer, Double> prices;
    private ConcurrentHashMap<Integer, String> categories;

    public BasketInfo(Basket basket)
    {
        this.shopid= basket.getShopid();
        this.products = new ConcurrentHashMap<>();
        this.prices = new ConcurrentHashMap<>();
        this.categories = new ConcurrentHashMap<>();
        for (int productid: basket.getProducts().keySet())
        {
            this.products.put(productid,basket.getProducts().get(productid));
            this.prices.put(productid,basket.getPrices().get(productid));
            this.categories.put(productid,basket.getCategories().get(productid));
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

    public ConcurrentHashMap<Integer, String> getCategories() {
        return categories;
    }
}
