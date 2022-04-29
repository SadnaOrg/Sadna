package BusinessLayer.Shops;


import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ShopImpl implements Shop {
    public ShopImpl(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public enum State {
        OPEN,
        CLOSED
    }


    private int id;
    private String name;
    private State state = State.OPEN;
    private ConcurrentHashMap<Integer, Product> products = new ConcurrentHashMap<>();

    public void addProduct(Product p) {
        products.put(p.getID(), p);
    }

    public void changeProduct(Product new_product) {
        if (products.containsKey(new_product.getID())) {
            Product old_product = products.get(new_product.getID());
            old_product.setPrice(new_product.getPrice());
            old_product.setQuantity(new_product.getQuantity());
            old_product.setName(new_product.getName());
        }
    }

    public void removeProduct(Product p) {
        products.remove(p.getID());
    }

    public ConcurrentHashMap<Integer, Product> getProducts() {
        return products;
    }

    public Collection<Product> searchProducts(ProductFilters pred){
        return products.values().stream().filter(pred).collect(Collectors.toList());
    }

}
