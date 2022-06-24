package ORM.Shops.Discounts;

import ORM.Shops.Product;
import ORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ProductByQuantityDiscounts")
public class ProductByQuantityDiscount extends DiscountPolicy{
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
    private int productQuantity;
    private double discount;

    public ProductByQuantityDiscount() {
    }

    public ProductByQuantityDiscount(Shop shop, int ID, DiscountPolicy policy, Product product, int productQuantity, double discount) {
        super(shop, ID, policy);
        this.product = product;
        this.productQuantity = productQuantity;
        this.discount = discount;
    }

    public ProductByQuantityDiscount(Shop shop, int ID, Product product, int productQuantity, double discount) {
        super(shop, ID);
        this.product = product;
        this.productQuantity = productQuantity;
        this.discount = discount;
    }

}
