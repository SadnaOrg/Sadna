package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public interface LogicDiscountRules extends DiscountRules {

    public double calculateDiscount(Basket basket);
}
