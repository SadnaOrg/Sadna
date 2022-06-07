package com.SadnaORM.Shops.Discounts;

import com.SadnaORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ShopDiscounts")
public class ShopDiscount extends DiscountPolicy{
    private int basketQuantity;
    private double discount;

    public ShopDiscount() {
    }

    public ShopDiscount(Shop shop, int ID, DiscountPolicy policy, int basketQuantity, int discount) {
        super(shop, ID, policy);
        this.basketQuantity = basketQuantity;
        this.discount = discount;
    }

    public ShopDiscount(Shop shop, int ID, int basketQuantity, int discount) {
        super(shop, ID);
        this.basketQuantity = basketQuantity;
        this.discount = discount;
    }

}
