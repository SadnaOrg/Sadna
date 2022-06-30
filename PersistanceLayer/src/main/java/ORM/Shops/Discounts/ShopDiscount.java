package ORM.Shops.Discounts;

import ORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ShopDiscount")
public class ShopDiscount extends DiscountPolicy{
    private int basketQuantity;
    private double discount;

    public ShopDiscount() {
    }

    public int getBasketQuantity() {
        return basketQuantity;
    }

    public void setBasketQuantity(int basketQuantity) {
        this.basketQuantity = basketQuantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public ShopDiscount(Shop shop, int ID, int basketQuantity, double discount) {
        super(shop, ID);
        this.basketQuantity = basketQuantity;
        this.discount = discount;
    }

    public ShopDiscount(int basketQuantity, double discount) {
        this.basketQuantity = basketQuantity;
        this.discount = discount;
    }

}
