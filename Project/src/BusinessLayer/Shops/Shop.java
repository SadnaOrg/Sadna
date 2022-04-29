package BusinessLayer.Shops;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import BusinessLayer.Products.*;
public interface Shop {

    ConcurrentHashMap<Integer, Product> getProducts();
    void addProduct(Product p);
    void changeProduct(Product new_product);
    void removeProduct(Product p);

    Collection<Product> searchProducts(ProductFilters productFilters);
}