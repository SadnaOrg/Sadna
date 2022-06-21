package com.SadnaORM.ShopImpl.ShopObjects.Discounts;

public class ProductDiscountDTO extends DiscountPolicyDTO{
    private int productID;
    private double discount;

    public ProductDiscountDTO(int ID, int shopID, int productID, double discount) {
        super(ID, shopID);
        this.productID = productID;
        this.discount = discount;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
