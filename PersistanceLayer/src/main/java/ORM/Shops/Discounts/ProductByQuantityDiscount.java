package ORM.Shops.Discounts;


import ORM.Shops.Product;
import ORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ProductByQuantityDiscount")
public class ProductByQuantityDiscount extends DiscountPolicy{
    @ManyToOne
    private Product product;
    private int productQuantity;
    private double discount;

    public ProductByQuantityDiscount() {
    }

    public ProductByQuantityDiscount(Shop shop, int ID, Product product, int productQuantity, double discount) {
        super(shop, ID);
        this.product = product;
        this.productQuantity = productQuantity;
        this.discount = discount;
    }

    public ProductByQuantityDiscount(Product product, int productQuantity, double discount) {
        this.product = product;
        this.productQuantity = productQuantity;
        this.discount = discount;
    }

    public Product getProduct() {
        return product;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public double getDiscount() {
        return discount;
    }
}