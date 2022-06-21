package com.SadnaORM.ShopImpl.ShopObjects.Discounts;

public class ValidateProductQuantityDiscountDTO extends DiscountPredDTO{
    private int productID;
    private int productQuantity;

    public ValidateProductQuantityDiscountDTO(int ID, int shopID, int productID, int productQuantity) {
        super(ID, shopID);
        this.productID = productID;
        this.productQuantity = productQuantity;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
}
