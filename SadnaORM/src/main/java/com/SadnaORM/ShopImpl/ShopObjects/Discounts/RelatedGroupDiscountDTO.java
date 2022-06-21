package com.SadnaORM.ShopImpl.ShopObjects.Discounts;

public class RelatedGroupDiscountDTO extends DiscountPolicyDTO{
    private String category;
    private double discount;

    public RelatedGroupDiscountDTO(int ID, int shopID, String category, double discount) {
        super(ID, shopID);
        this.category = category;
        this.discount = discount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
