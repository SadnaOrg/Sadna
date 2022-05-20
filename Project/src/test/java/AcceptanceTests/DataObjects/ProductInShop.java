package AcceptanceTests.DataObjects;

public class ProductInShop {
    public int ID;
    public int shopID;
    public int quantity;
    public double price;
    public String desc;
    public Product product;

    public ProductInShop(int ID,int shopID,int quantity,double price, Product product){
        this.ID = ID;
        this.shopID = shopID;
        this.quantity = quantity;
        this.price = price;
        this.desc = product.desc;
        this.product = product;
    }

    public ProductInShop(ServiceLayer.Objects.Product product){
        this.ID = product.productID();
        this.shopID = product.shopId();
        this.quantity = product.quantity();
        this.price = product.price();
        this.desc = product.description();
        this.product = new Product(product.name(),product.description(),product.manufacturer());
    }

    public String getDescription() {
        return this.product.desc;
    }

    public String getManufacturer(){
        return this.product.manufacturer;
    }
}
