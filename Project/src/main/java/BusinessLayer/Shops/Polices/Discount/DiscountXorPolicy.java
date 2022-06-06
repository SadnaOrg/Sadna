package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.Collection;

public class DiscountXorPolicy implements LogicDiscountRules{

    private Collection<DiscountPolicy> discountPolicy;
    private Collection<DiscountPred> discountPreds;

    @Override
    public double calculateDiscount(Basket basket) {

    }
}
