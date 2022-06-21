package com.SadnaORM.ShopImpl.ShopObjects.Discounts;

public class ValidateBasketValueDiscountDTO extends DiscountPredDTO{
    double basketvalue;
    boolean cantBeMore;

    public double getBasketvalue() {
        return basketvalue;
    }

    public void setBasketvalue(double basketvalue) {
        this.basketvalue = basketvalue;
    }

    public boolean isCantBeMore() {
        return cantBeMore;
    }

    public void setCantBeMore(boolean cantBeMore) {
        this.cantBeMore = cantBeMore;
    }

    public ValidateBasketValueDiscountDTO(int ID, int shopID, double basketvalue, boolean cantBeMore) {
        super(ID, shopID);
        this.basketvalue = basketvalue;
        this.cantBeMore = cantBeMore;
    }
}
