package com.SadnaORM.ShopImpl.ShopObjects.Discounts;

import com.SadnaORM.Shops.Product;

public class ProductQuantityInPriceDiscountDTO extends DiscountPolicyDTO{
    private int productID;
    private int quantity;
    private double priceForQuantity;

    public ProductQuantityInPriceDiscountDTO(int ID, int shopID, int productID, int quantity, double priceForQuantity) {
        super(ID, shopID);
        this.productID = productID;
        this.quantity = quantity;
        this.priceForQuantity = priceForQuantity;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceForQuantity() {
        return priceForQuantity;
    }

    public void setPriceForQuantity(double priceForQuantity) {
        this.priceForQuantity = priceForQuantity;
    }
}
