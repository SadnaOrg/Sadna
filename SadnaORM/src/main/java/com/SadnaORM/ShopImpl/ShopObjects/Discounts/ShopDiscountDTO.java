package com.SadnaORM.ShopImpl.ShopObjects.Discounts;


public class ShopDiscountDTO extends DiscountPolicyDTO {
    private int basketQuantity;
    private double discount;

    public ShopDiscountDTO() {
    }

    public ShopDiscountDTO(int shop, int ID, DiscountPolicyDTO policy, int basketQuantity, int discount) {
        super(shop, ID, policy);
        this.basketQuantity = basketQuantity;
        this.discount = discount;
    }

    public ShopDiscountDTO(int shop, int ID, int basketQuantity, int discount) {
        super(shop, ID);
        this.basketQuantity = basketQuantity;
        this.discount = discount;
    }

}
