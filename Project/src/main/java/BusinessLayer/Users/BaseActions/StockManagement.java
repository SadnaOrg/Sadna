package BusinessLayer.Users.BaseActions;

import BusinessLayer.Mappers.ProductMapper;
import BusinessLayer.Products.Product;
import BusinessLayer.Shops.Shop;

public class StockManagement extends BaseAction {

    private Shop shop;

    public StockManagement(Shop shop) {
        this.shop= shop;
    }

    public Product addProduct(int productid, String name, String desc, String manufacturer, double price, int quantity)
    {
        if(quantity <= 0)
            throw new IllegalStateException("quantity in stock must be positive when adding a product!");
        if(price <= 0)
            throw new IllegalStateException("the price of a product has to be positive!");
        Product product = new Product(productid,name,price,quantity);
        product.setDescription(desc);
        product.setManufacturer(manufacturer);
        shop.addProduct(product);
        return product;
    }

    public boolean setCategory(int productId, String category) {
        if(category== null)
            throw new IllegalStateException("must be real category");
        return shop.setCategory(productId,category);
    }

    public void removeProduct(int productid)
    {
        shop.removeProduct(productid);
    }

    public boolean changeProductQuantity(int productid, int newQuantity)
    {
        if(shop.getProducts().containsKey(productid)) {
            Product product = shop.getProducts().get(productid);
            product.setQuantity(newQuantity);
            return true;
        }
        return false;
    }

    public boolean changeProductPrice(int productid, double newPrice)
    {
        if(shop.getProducts().containsKey(productid)) {
            Product product = shop.getProducts().get(productid);
            product.setPrice(newPrice);
            return true;
        }
        return false;
    }

    public boolean changeProductDesc(int productid, String newDesc)
    {
        if(shop.getProducts().containsKey(productid)) {
            Product product = shop.getProducts().get(productid);
            product.setDescription(newDesc);
            return true;
        }
        return false;
    }

    public boolean changeProductName(int productid, String newName)
    {
        if(shop.getProducts().containsKey(productid)) {
            Product product = shop.getProducts().get(productid);
            product.setName(newName);
            return true;
        }
        return false;
    }
}
