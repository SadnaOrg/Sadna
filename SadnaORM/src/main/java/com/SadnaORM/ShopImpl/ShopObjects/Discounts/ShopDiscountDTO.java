package com.SadnaORM.ShopImpl.ShopObjects.Discounts;

public class ShopDiscountDTO extends DiscountPolicyDTO{
    private int basketQuantity;
    private double discount;

    public ShopDiscountDTO(int ID, int shopID, int basketQuantity, double discount) {
        super(ID, shopID);
        this.basketQuantity = basketQuantity;
        this.discount = discount;
    }

    public int getBasketQuantity() {
        return basketQuantity;
    }

    public void setBasketQuantity(int basketQuantity) {
        this.basketQuantity = basketQuantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
