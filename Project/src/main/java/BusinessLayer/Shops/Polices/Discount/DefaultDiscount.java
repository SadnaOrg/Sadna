package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class DefaultDiscount implements DiscountPolicyInterface{
    @Override
    public double calculateDiscount(Basket basket) {
        return 0;
    }

    @Override
    public int getDiscountId() {
        return 0;
    }

    @Override
    public DiscountPolicyInterface getDiscountPolicy() {
        return this;
    }
    @Override
    public DiscountPolicyInterface removeDiscountById(int id)
    {
        return this;
    }
    @Override
    public boolean removeDiscountByIdReq(int id)
    {
        return false;
    }
    @Override
    public DiscountPolicyInterface getDiscountById(int id){ return this; }
}
