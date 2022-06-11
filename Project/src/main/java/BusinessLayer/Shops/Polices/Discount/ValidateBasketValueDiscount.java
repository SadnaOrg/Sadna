package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class ValidateBasketValueDiscount implements DiscountPred {
    private int ruleId;
    private double basketvalue;
    private boolean cantBeMore;

    public ValidateBasketValueDiscount(int ruleId, double basketvalue, boolean cantBeMore) {
        this.ruleId = ruleId;
        this.basketvalue = basketvalue;
        this.cantBeMore = cantBeMore;
    }

    public ValidateBasketValueDiscount(double basketvalue , boolean cantBeMore) {
        this.basketvalue = basketvalue;
        this.ruleId = atomicRuleID.incrementAndGet();
        this.cantBeMore =cantBeMore;
    }

    @Override
    public boolean validateDiscount(Basket basket) {
        double currprice=0;
        for(int i: basket.getProducts().keySet())
        {
            currprice +=basket.getPrices().get(i)*basket.getProducts().get(i);
        }
        if(cantBeMore)
            return currprice<=basketvalue;
        return currprice>=basketvalue;
    }

    public int getRuleId() {
        return ruleId;
    }

    public double getBasketvalue() {
        return basketvalue;
    }

    public boolean isCantBeMore() {
        return cantBeMore;
    }
}
