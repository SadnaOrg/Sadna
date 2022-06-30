package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Mappers.Converter;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.Basket;

public class ValidateBasketValueDiscount implements DiscountPred {
    private int ruleId;

    double basketvalue;
    boolean cantBeMore;

    public ValidateBasketValueDiscount(double basketvalue ,boolean cantBeMore) {
        this.basketvalue = basketvalue;
        this.ruleId = atomicRuleID.incrementAndGet();
        this.cantBeMore =cantBeMore;
    }

    public ValidateBasketValueDiscount(int ruleId, double basketvalue, boolean cantBeMore) {
        this.ruleId = ruleId;
        this.basketvalue = basketvalue;
        this.cantBeMore = cantBeMore;
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

    @Override
    public int getID(){
        return this.ruleId;
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

    @Override
    public ORM.Shops.Discounts.DiscountPred toEntity(Converter c, ORM.Shops.Shop shop) {
        return c.toEntity(this,shop);
    }
}