package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class DefaultDiscount implements DiscountPolicyInterface{

    @Override
    public double calculateDiscount(Basket basket) {
        return 0;
    }
}
