package Shops;

import java.util.concurrent.ConcurrentHashMap;

public interface Shop {

    ConcurrentHashMap<Integer, Product> getProducts();
    void addProduct(Product p);
    void changeProduct(Product new_product);
    void removeProduct(Product p);
}
