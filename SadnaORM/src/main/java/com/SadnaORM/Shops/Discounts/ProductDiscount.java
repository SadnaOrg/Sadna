package com.SadnaORM.Shops.Discounts;

import com.SadnaORM.Shops.Product;
import com.SadnaORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ProductDiscount")
public class ProductDiscount extends DiscountPolicy{
    @ManyToOne
    private Product product;
    private double discount;

    public ProductDiscount() {
    }

    public ProductDiscount(Shop shop, int ID, Product product, double discount) {
        super(shop, ID);
        this.product = product;
        this.discount = discount;
    }

    public ProductDiscount(Product product, double discount) {
        this.product = product;
        this.discount = discount;
    }

    public Product getProduct() {
        return product;
    }

    public double getDiscount() {
        return discount;
    }
}
