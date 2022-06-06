package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public interface DiscountRules {

    public double calculateDiscount(Basket basket);

}
