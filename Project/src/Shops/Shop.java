package Shops;

public interface Shop {

    void addProduct(Product p);
    void changeProduct(Product old_product, Product new_product);
    void removeProduct(Product p);
}
