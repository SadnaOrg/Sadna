package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.Collection;

public class ValidateBasketQuantityDiscount implements DiscountPred{
    private int ruleId;

    private int basketquantity;
    boolean cantBeMore;

    public ValidateBasketQuantityDiscount(int basketquantity,boolean cantBeMore) {
        this.basketquantity = basketquantity;
        this.ruleId = atomicRuleID.incrementAndGet();
        this.cantBeMore =cantBeMore;

    }

    @Override
    public boolean validateDiscount(Basket basket) {
        int currQuantity=0;
        for (int i: basket.getProducts().keySet())
        {
            currQuantity+= basket.getProducts().get(i);
        }
        if(cantBeMore)
            return currQuantity<=basketquantity;
        return currQuantity>=basketquantity;
    }
}
