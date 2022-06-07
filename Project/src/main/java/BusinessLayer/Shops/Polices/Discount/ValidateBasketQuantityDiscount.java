package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.Collection;

public class ValidateBasketQuantityDiscount implements DiscountPred{
    private int ruleId;

    private int basketquantity;

    public ValidateBasketQuantityDiscount(int basketquantity) {
        this.basketquantity = basketquantity;
        this.ruleId = atomicRuleID.incrementAndGet();
    }

    @Override
    public boolean validateDiscount(Basket basket) {
        int currQuantity=0;
        for (int i: basket.getProducts().keySet())
        {
            currQuantity+= basket.getProducts().get(i);
        }
        return currQuantity>=basketquantity;
    }
}
