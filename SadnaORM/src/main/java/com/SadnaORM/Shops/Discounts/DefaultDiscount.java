package com.SadnaORM.Shops.Discounts;

import com.SadnaORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "DefaultDiscounts")
public class DefaultDiscount extends DiscountPolicy{
    private double discount;

    public DefaultDiscount() {
    }

    public DefaultDiscount(Shop shop, int ID, double discount) {
        super(shop, ID);
        this.discount = discount;
    }

    public DefaultDiscount(Shop shop, int ID){
        super(shop,ID);
        this.discount = 0;
    }

}
