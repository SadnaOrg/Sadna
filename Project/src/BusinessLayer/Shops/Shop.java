package BusinessLayer.Shops;


import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Shop {
    public Shop(int id, String name) {
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

    public double purchaseBasket(ConcurrentHashMap<Integer, Integer> basketProducts) {
        int totalPrice = 0;
        for (int productID : basketProducts.keySet()) {
            int quantity = basketProducts.get(productID);
            if (products.containsKey(productID)) {
                Product curr_product = products.get(productID);
                double currentPrice = curr_product.purchaseProduct(quantity);
                if (currentPrice == 0.0)
                    return currentPrice;
                else
                    totalPrice += currentPrice;
            }
        }
        return totalPrice;
    }

    public int getId() {
        return id;
    }
}
