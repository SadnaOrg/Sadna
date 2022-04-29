package BusinessLayer.Products;

public interface  Product {
    int getID();
    String getName();
    double getPrice();
    int getQuantity();

    void setPrice(double price);

    void setQuantity(int quantity);

    void setName(String name);
}
