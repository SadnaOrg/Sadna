package com.SadnaORM.ShopImpl.ShopObjects.Discounts;

import com.SadnaORM.ShopImpl.ShopObjects.ProductDTO;
import com.SadnaORM.Shops.Product;


public class ProductQuantityInPriceDiscountDTO extends DiscountPolicyDTO {

    private ProductDTO product;
    private int quantity;
    private double priceForQuantity;

    public ProductQuantityInPriceDiscountDTO() {
    }

    public ProductQuantityInPriceDiscountDTO(int shop, int ID, DiscountPolicyDTO policy, ProductDTO product, int quantity, double priceForQuantity) {
        super(shop, ID, policy);
        this.product = product;
        this.quantity = quantity;
        this.priceForQuantity = priceForQuantity;
    }

    public ProductQuantityInPriceDiscountDTO(int shop, int ID, ProductDTO product, int quantity, double priceForQuantity) {
        super(shop, ID);
        this.product = product;
        this.quantity = quantity;
        this.priceForQuantity = priceForQuantity;
    }

}
