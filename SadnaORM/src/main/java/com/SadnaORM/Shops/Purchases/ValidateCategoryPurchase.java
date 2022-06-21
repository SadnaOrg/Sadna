package com.SadnaORM.Shops.Purchases;

import com.SadnaORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ValidateCategoryPurchase")
public class ValidateCategoryPurchase extends PurchasePolicy{
    private String category;
    private int productQuantity;
    //if true then can't be higher than false can't be lower than
    private boolean cantbemore;

    public ValidateCategoryPurchase() {
    }

    public ValidateCategoryPurchase(String category, int productQuantity, boolean cantbemore) {
        this.category = category;
        this.productQuantity = productQuantity;
        this.cantbemore = cantbemore;
    }

    public ValidateCategoryPurchase(Shop shop, int ID, String category, int productQuantity, boolean cantbemore) {
        super(shop, ID);
        this.category = category;
        this.productQuantity = productQuantity;
        this.cantbemore = cantbemore;
    }

    public String getCategory() {
        return category;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public boolean isCantbemore() {
        return cantbemore;
    }
}
