package AcceptanceTests.DataObjects;

import ServiceLayer.Objects.ProductInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ShopBasket {
    public Map<Integer, Integer> products;
    public Map<Integer,Double> prices;

    public ShopBasket(Map<Integer,Integer> products){
        this.products = products;
        this.prices = new HashMap<>();
    }

    public ShopBasket(ServiceLayer.Objects.Basket basket){
        this.products = new HashMap<>();
        this.prices = new HashMap<>();
        Collection<ProductInfo> infos = basket.productsID();
        for (ProductInfo info:
             infos) {
            products.put(info.Id(),info.quantity());
            prices.put(info.Id(),info.price());
        }
    }

    public int numOfProducts() {
        int size = 0;
        for (Integer id:
             products.keySet()) {
            size += products.get(id);
        }
        return size;
    }

    public int getProductQuantity(int productID) {
        return products.getOrDefault(productID,null);
    }
}
