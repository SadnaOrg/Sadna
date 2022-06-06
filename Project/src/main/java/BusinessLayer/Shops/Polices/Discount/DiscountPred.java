package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public interface DiscountPred{
    public boolean validateDiscount(Basket basket);
}
