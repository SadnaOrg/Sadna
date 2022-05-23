package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public interface DiscountPolicyInterface {

    double calculateDiscount(Basket basket);
}
