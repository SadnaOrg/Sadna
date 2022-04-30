package Mocks;

public class ProductInShop {
    public int ID;
    public int shopID;
    public int quantity;
    public double price;
    public double rating;
    public Product product;

    public ProductInShop(int ID,int shopID,int quantity,double price,double rating, Product product){
        this.ID = ID;
        this.shopID = shopID;
        this.quantity = quantity;
        this.price = price;
        this.rating = rating;
        this.product = product;
    }

    public String getManufacturer() {
        return this.product.manufacturer;
    }
}
