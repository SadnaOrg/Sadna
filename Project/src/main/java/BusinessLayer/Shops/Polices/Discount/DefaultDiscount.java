package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class DefaultDiscount extends DiscountPolicy{
    public DefaultDiscount()
    {
        super();
    }


    @Override
    double calculateDiscount(Basket basket) {
        return 0;
    }
}
