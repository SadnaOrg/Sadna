package ORM.Shops.Discounts;

import ORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ValidateBasketValueDiscount")
public class ValidateBasketValueDiscount extends DiscountPred{
    double basketvalue;
    boolean cantBeMore;

    public ValidateBasketValueDiscount() {
    }

    public ValidateBasketValueDiscount(Shop shop, int ID, double basketvalue, boolean cantBeMore) {
        super(shop, ID);
        this.basketvalue = basketvalue;
        this.cantBeMore = cantBeMore;
    }

    public ValidateBasketValueDiscount(double basketvalue, boolean cantBeMore) {
        this.basketvalue = basketvalue;
        this.cantBeMore = cantBeMore;
    }

    public double getBasketvalue() {
        return basketvalue;
    }

    public boolean isCantBeMore() {
        return cantBeMore;
    }
}