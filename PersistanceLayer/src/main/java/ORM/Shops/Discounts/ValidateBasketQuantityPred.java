package ORM.Shops.Discounts;

import ORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ValidateBasketQuantityPred")
public class ValidateBasketQuantityPred extends DiscountPred{
    private int basketquantity;
    boolean cantBeMore;

    public ValidateBasketQuantityPred() {
    }

    public ValidateBasketQuantityPred(Shop shop, int ID, int basketquantity, boolean cantBeMore) {
        super(shop, ID);
        this.basketquantity = basketquantity;
        this.cantBeMore = cantBeMore;
    }

    public ValidateBasketQuantityPred(int basketquantity, boolean cantBeMore) {
        this.basketquantity = basketquantity;
        this.cantBeMore = cantBeMore;
    }

    public int getBasketquantity() {
        return basketquantity;
    }

    public boolean isCantBeMore() {
        return cantBeMore;
    }
}
