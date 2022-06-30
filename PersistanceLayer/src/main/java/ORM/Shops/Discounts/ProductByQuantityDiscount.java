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

    public ProductByQuantityDiscount(Shop shop, int ID, Product product, int productQuantity, double discount) {
        super(shop, ID);
        this.product = product;
        this.productQuantity = productQuantity;
        this.discount = discount;
    }

    public ProductByQuantityDiscount(int productId, int productQuantity, double discount) {
        this.product = product;
        this.productQuantity = productQuantity;
        this.discount = discount;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public ProductByQuantityDiscount(Shop shop, int ID) {
        super(shop, ID);
    }

    public ProductByQuantityDiscount() {
    }
}
