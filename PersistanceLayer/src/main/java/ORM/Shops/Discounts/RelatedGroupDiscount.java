package ORM.Shops.Discounts;


import ORM.Shops.Product;
import ORM.Shops.Shop;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "RelatedGroupDiscounts")
public class RelatedGroupDiscount extends DiscountPolicy{
    @ManyToMany
    @JoinTable(name="RelatedGroupDiscountsProducts", joinColumns={
            @JoinColumn(name="SHOP_ID"),
            @JoinColumn(name = "POLICY_ID")
    },
            inverseJoinColumns=@JoinColumn(name="product_id"))
    Collection<Product> relatedProducts;
    double discount;

    public RelatedGroupDiscount() {
    }

    public RelatedGroupDiscount(Shop shop, int ID, DiscountPolicy policy, Collection<Product> relatedProducts, double discount) {
        super(shop, ID, policy);
        this.relatedProducts = relatedProducts;
        this.discount = discount;
    }

    public RelatedGroupDiscount(Shop shop, int ID, Collection<Product> relatedProducts, double discount) {
        super(shop, ID);
        this.relatedProducts = relatedProducts;
        this.discount = discount;
    }

}
