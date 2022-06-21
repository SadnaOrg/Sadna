package com.SadnaORM.Shops.Purchases;

import com.SadnaORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ValidateUserPurchase")
public class ValidateUserPurchase extends PurchasePolicy{
    private int age;

    public ValidateUserPurchase() {
    }

    public ValidateUserPurchase(int age) {
        this.age = age;
    }

    public ValidateUserPurchase(Shop shop, int ID, int age) {
        super(shop, ID);
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}
