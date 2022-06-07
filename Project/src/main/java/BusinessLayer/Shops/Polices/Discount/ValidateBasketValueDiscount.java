package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class ValidateBasketValueDiscount implements DiscountPred {
    private int ruleId;

    double basketvalue;

    public ValidateBasketValueDiscount(double basketvalue) {
        this.basketvalue = basketvalue;
        this.ruleId = atomicRuleID.incrementAndGet();
    }

    @Override
    public boolean validateDiscount(Basket basket) {
        double currprice=0;
        for(int i: basket.getProducts().keySet())
        {
            currprice +=basket.getPrices().get(i)*basket.getProducts().get(i);
        }
        return currprice>=basketvalue;
    }
}
