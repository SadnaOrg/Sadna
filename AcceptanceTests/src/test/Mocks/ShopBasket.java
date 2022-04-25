package test.Mocks;

import java.util.Map;

public class ShopBasket {
    public Map<Integer, Integer> products;
    public double price;

    public ShopBasket(Map<Integer,Integer> products,double price){
        this.products = products;
        this.price = price;
    }

    public int numOfProducts() {
        return products.size();
    }
}
