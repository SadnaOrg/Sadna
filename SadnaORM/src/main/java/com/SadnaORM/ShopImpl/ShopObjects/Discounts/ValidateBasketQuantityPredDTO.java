package com.SadnaORM.ShopImpl.ShopObjects.Discounts;

public class ValidateBasketQuantityPredDTO extends DiscountPredDTO{
    private int basketquantity;
    boolean cantBeMore;

    public ValidateBasketQuantityPredDTO(int ID, int shopID, int basketquantity, boolean cantBeMore) {
        super(ID, shopID);
        this.basketquantity = basketquantity;
        this.cantBeMore = cantBeMore;
    }

    public int getBasketquantity() {
        return basketquantity;
    }

    public void setBasketquantity(int basketquantity) {
        this.basketquantity = basketquantity;
    }

    public boolean isCantBeMore() {
        return cantBeMore;
    }

    public void setCantBeMore(boolean cantBeMore) {
        this.cantBeMore = cantBeMore;
    }
}
