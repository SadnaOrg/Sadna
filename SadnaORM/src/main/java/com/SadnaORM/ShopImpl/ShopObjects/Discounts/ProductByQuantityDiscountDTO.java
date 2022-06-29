package com.SadnaORM.ShopImpl.ShopObjects.Discounts;

import com.SadnaORM.ShopImpl.ShopObjects.ProductDTO;
import com.SadnaORM.Shops.Product;


public class ProductByQuantityDiscountDTO extends DiscountPolicyDTO {

    private ProductDTO product;
    private int productQuantity;
    private double discount;

    public ProductByQuantityDiscountDTO() {
    }

    public ProductByQuantityDiscountDTO(int shop, int ID, DiscountPolicyDTO policy, ProductDTO product, int productQuantity, double discount) {
        super(shop, ID, policy);
        this.product = product;
        this.productQuantity = productQuantity;
        this.discount = discount;
    }

    public ProductByQuantityDiscountDTO(int shop, int ID, ProductDTO product, int productQuantity, double discount) {
        super(shop, ID);
        this.product = product;
        this.productQuantity = productQuantity;
        this.discount = discount;
    }

}
