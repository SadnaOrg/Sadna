package Shops;


import Products.Product;
import Products.ProductFilters;

import java.util.Collection;

public class Shop {

    public enum State {
        OPEN,
        CLOSED
    }

    private int id;
    private String name;
    private State state;

    public Collection<Product> searchProducts(ProductFilters pred){
        throw new UnsupportedOperationException("not implement yet");
    }
}
