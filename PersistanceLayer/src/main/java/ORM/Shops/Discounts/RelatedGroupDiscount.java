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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
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
}
