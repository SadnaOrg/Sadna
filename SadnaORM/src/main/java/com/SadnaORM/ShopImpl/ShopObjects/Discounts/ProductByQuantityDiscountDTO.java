package com.SadnaORM.ShopImpl.ShopObjects.Discounts;

public class ProductByQuantityDiscountDTO extends DiscountPolicyDTO{
    private int productID;
    private int productQuantity;
    private double discount;

    public ProductByQuantityDiscountDTO(int ID, int shopID, int productID, int productQuantity, double discount) {
        super(ID, shopID);
        this.productID = productID;
        this.productQuantity = productQuantity;
        this.discount = discount;
    }

    public int getProductID() {
        return productID;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
