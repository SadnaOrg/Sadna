package ORM.Shops.Discounts;

import ORM.Shops.Product;
import ORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ProductDiscount")
public class ProductDiscount extends DiscountPolicy{
    @ManyToOne
    private Product product;
    private double discount;


    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public ProductDiscount(Shop shop, int ID) {
        super(shop, ID);
    }

    public ProductDiscount() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductDiscount(Shop shop, int ID, Product product, double discount) {
        super(shop, ID);
        this.product = product;
        this.discount = discount;
    }

    public ProductDiscount(Product product, double discount) {
        this.product = product;
        this.discount = discount;
    }
}
