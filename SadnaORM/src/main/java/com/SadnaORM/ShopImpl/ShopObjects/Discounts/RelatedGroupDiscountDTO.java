package com.SadnaORM.ShopImpl.ShopObjects.Discounts;

import com.SadnaORM.ShopImpl.ShopObjects.ProductDTO;
import com.SadnaORM.Shops.Product;

import java.util.Collection;

public class RelatedGroupDiscountDTO extends DiscountPolicyDTO {

    Collection<ProductDTO> relatedProducts;
    double discount;

    public RelatedGroupDiscountDTO() {
    }

    public RelatedGroupDiscountDTO(int shop, int ID, DiscountPolicyDTO policy, Collection<ProductDTO> relatedProducts, double discount) {
        super(shop, ID, policy);
        this.relatedProducts = relatedProducts;
        this.discount = discount;
    }

    public RelatedGroupDiscountDTO(int shop, int ID, Collection<ProductDTO> relatedProducts, double discount) {
        super(shop, ID);
        this.relatedProducts = relatedProducts;
        this.discount = discount;
    }

}
