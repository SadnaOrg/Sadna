package ORM.Shops.Discounts;


import ORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "RelatedGroupDiscount")
public class RelatedGroupDiscount extends DiscountPolicy{
    private String category;
    private double discount;

    public RelatedGroupDiscount() {
    }

    public RelatedGroupDiscount(Shop shop, int ID, String category, double discount) {
        super(shop, ID);
        this.category = category;
        this.discount = discount;
    }

    public RelatedGroupDiscount(String category, double discount) {
        this.category = category;
        this.discount = discount;
    }

    public String getCategory() {
        return category;
    }

    public double getDiscount() {
        return discount;
    }
}