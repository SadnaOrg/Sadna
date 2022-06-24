package ORM.Shops.Discounts;

import ORM.Shops.Product;
import ORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ProductDiscounts")
public class ProductDiscount extends DiscountPolicy{
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
    double discount;

    public ProductDiscount() {
    }

    public ProductDiscount(Shop shop, int ID, DiscountPolicy policy, Product product, double discount) {
        super(shop, ID, policy);
        this.product = product;
        this.discount = discount;
    }

    public ProductDiscount(Shop shop, int ID, Product product, double discount) {
        super(shop, ID);
        this.product = product;
        this.discount = discount;
    }

}
