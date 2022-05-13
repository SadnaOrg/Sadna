package AcceptanceTests.DataObjects;

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

    public int getProductQuantity(int productID) {
        return products.getOrDefault(productID,null);
    }
}
