package com.SadnaORM.ShopImpl.ShopObjects.Discounts;


public class DefaultDiscountDTO extends DiscountPolicyDTO {
    private double discount;

    public DefaultDiscountDTO() {
    }

    public DefaultDiscountDTO(int shop, int ID, double discount) {
        super(shop, ID);
        this.discount = discount;
    }

    public DefaultDiscountDTO(int shop, int ID){
        super(shop,ID);
        this.discount = 0;
    }

}
