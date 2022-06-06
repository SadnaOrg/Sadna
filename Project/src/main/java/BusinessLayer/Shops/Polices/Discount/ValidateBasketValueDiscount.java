package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class ValidateBasketValueDiscount implements DiscountPred {

    double basketvalue;

    public ValidateBasketValueDiscount(double basketvalue) {
        this.basketvalue = basketvalue;
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
