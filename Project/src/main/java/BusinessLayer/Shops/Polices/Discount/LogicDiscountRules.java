package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public interface LogicDiscountRules extends DiscountPred {

    public boolean validateDiscount(Basket basket);
    public void add(DiscountPred discountPred);
    public boolean remove(DiscountPred discountPred);
}
