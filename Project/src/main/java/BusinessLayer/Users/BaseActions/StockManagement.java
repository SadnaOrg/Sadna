package BusinessLayer.Users.BaseActions;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.BaseAction;

public class StockManagement extends BaseAction {

    private Shop shop;

    public StockManagement(Shop shop) {
        this.shop= shop;
    }

    public Product addProduct(int productid, String name, double price, int quantity)
    {
        Product product = new Product(productid,name,price,quantity);
        shop.addProduct(product);
        return product;
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

    public boolean changeProductPrice(int productid, int newPrice)
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
