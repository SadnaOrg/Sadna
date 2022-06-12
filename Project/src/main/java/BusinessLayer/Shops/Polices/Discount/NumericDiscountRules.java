package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public interface NumericDiscountRules extends DiscountRules {

    public double calculateDiscount(Basket basket);
    public void add(DiscountRules discountRules);
    public boolean remove(DiscountRules discountRules);
    public boolean removeSonDiscount(DiscountRules removeFromConnectId);

}
