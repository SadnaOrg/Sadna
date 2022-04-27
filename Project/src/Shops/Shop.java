package Shops;


import java.util.HashMap;

public class Shop {
    public enum State {
        OPEN,
        CLOSED
    }
    private int id;
    private String name;
    private State state;
    private HashMap<Integer, Product> products;

    public void addProduct(Product p) {
        products.put(p.getID(), p);
    }

    public void changeProduct(Product old_product, Product new_product) {
        products.remove(old_product.getID());
        products.put(new_product.getID(), new_product);
    }

    public void removeProduct(Product p) {
        products.remove(p.getID());
    }
}
