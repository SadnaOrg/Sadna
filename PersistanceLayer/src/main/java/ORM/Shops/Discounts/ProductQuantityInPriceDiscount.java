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
    private Product p;
    private int quantity;
    private double priceForQuantity;

    public ProductQuantityInPriceDiscount() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceForQuantity() {
        return priceForQuantity;
    }

    public void setPriceForQuantity(double priceForQuantity) {
        this.priceForQuantity = priceForQuantity;
    }

    public Product getP() {
        return p;
    }

    public void setP(Product p) {
        this.p = p;
    }

    public ProductQuantityInPriceDiscount(Shop shop, int ID, Product p, int quantity, double priceForQuantity) {
        super(shop, ID);
        this.p = p;
        this.quantity = quantity;
        this.priceForQuantity = priceForQuantity;
    }

    public ProductQuantityInPriceDiscount(Product p, int quantity, double priceForQuantity) {
        this.p = p;
        this.quantity = quantity;
        this.priceForQuantity = priceForQuantity;
    }
}
