package com.SadnaORM.Shops.Discounts;

import com.SadnaORM.Shops.Product;
import com.SadnaORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ProductQuantityInPriceDiscounts")
public class ProductQuantityInPriceDiscount extends DiscountPolicy{
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
    private int quantity;
    private double priceForQuantity;

    public ProductQuantityInPriceDiscount() {
    }

    public ProductQuantityInPriceDiscount(Shop shop, int ID, DiscountPolicy policy, Product product, int quantity, double priceForQuantity) {
        super(shop, ID, policy);
        this.product = product;
        this.quantity = quantity;
        this.priceForQuantity = priceForQuantity;
    }

    public ProductQuantityInPriceDiscount(Shop shop, int ID, Product product, int quantity, double priceForQuantity) {
        super(shop, ID);
        this.product = product;
        this.quantity = quantity;
        this.priceForQuantity = priceForQuantity;
    }

}
