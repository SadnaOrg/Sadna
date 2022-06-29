package BusinessLayer.Products;



public class Product {

    private int id;
    private String name;
    private String description;
    private String manufacturer;
    private String category;
    private double price;
    private int quantity;
    public Product(int id, String name, String description, String manufacturer, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.price = price;
        this.quantity = quantity;
        this.category = null;
    }

    public Product(int id, String name, String description, String manufacturer, String category, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.category = null;
    }

    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = null;
    }


    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double purchaseProduct(int buyQuantity) {
        if (quantity - buyQuantity >= 0) {
            quantity -= buyQuantity;
            return buyQuantity * price;
        }
        throw new IllegalStateException("Try to buy out of stock product from the shop");
    }

    public double checkIfCanBuy(int buyQuantity) {
        if (quantity - buyQuantity >= 0) {
            return buyQuantity * price;
        }
        throw new IllegalStateException("Try to buy out of stock product from the shop");
    }

    public void setPrice(double price) {
        if(price < 0)
            throw new IllegalArgumentException("a product can't have a negative price!");
        this.price = price;
    }

    public void setQuantity(int quantity) {
        if(quantity < 0)
            throw new IllegalArgumentException("product can't have a negative quantity!");
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String  getManufacturer() {
        return this.manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
