package com.SadnaORM.ShopImpl.ShopObjects.Discounts;

import com.SadnaORM.ShopImpl.ShopObjects.ProductDTO;
import com.SadnaORM.Shops.Product;


public class ProductDiscountDTO extends DiscountPolicyDTO {
    private ProductDTO product;
    double discount;

    public ProductDiscountDTO() {
    }

    public ProductDiscountDTO(int shop, int ID, DiscountPolicyDTO policy, ProductDTO product, double discount) {
        super(shop, ID, policy);
        this.product = product;
        this.discount = discount;
    }

    public ProductDiscountDTO(int shop, int ID, ProductDTO product, double discount) {
        super(shop, ID);
        this.product = product;
        this.discount = discount;
    }

}
