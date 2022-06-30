package ORM.Shops.Discounts;

import ORM.Shops.Product;
import ORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ProductQuantityInPriceDiscount")
public class ProductQuantityInPriceDiscount extends DiscountPolicy{
    @ManyToOne
    private Product product;
    private int quantity;
    private double priceForQuantity;

    public ProductQuantityInPriceDiscount() {
    }

    public ProductQuantityInPriceDiscount(Shop shop, int ID, Product product, int quantity, double priceForQuantity) {
        super(shop, ID);
        this.product = product;
        this.quantity = quantity;
        this.priceForQuantity = priceForQuantity;
    }

    public ProductQuantityInPriceDiscount(Product product, int quantity, double priceForQuantity) {
        this.product = product;
        this.quantity = quantity;
        this.priceForQuantity = priceForQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPriceForQuantity() {
        return priceForQuantity;
    }
}