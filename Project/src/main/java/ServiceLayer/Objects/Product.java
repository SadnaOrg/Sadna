package ServiceLayer.Objects;

import BusinessLayer.Products.ProductInfo;

public record Product(int shopId, int productID,String name,String description,double price, int quantity,String manufacturer, String category) {
    public Product(int shopID,ProductInfo p) {
        this(shopID,p.getProductid(),p.getProductname(),p.getProductdescription(),p.getProductprice(),p.getProductquantity(),p.getManufacturer(), p.getCategory());
    }

    public Product(int shopID, BusinessLayer.Products.Product p) {
        this(shopID,p.getID(),p.getName(),p.getDescription(),p.getPrice(),p.getQuantity(),p.getManufacturer(), p.getCategory());
    }

    public Product(BusinessLayer.Products.Product p) {
        this(-1,p.getID(),p.getName(),p.getDescription(),p.getPrice(),p.getQuantity(),p.getManufacturer(), p.getCategory());
    }
}
