package com.SadnaORM.Shops.Discounts;

import com.SadnaORM.Shops.Product;
import com.SadnaORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ValidateProductQuantityDiscount")
public class ValidateProductQuantityDiscount extends DiscountPred{
    @ManyToOne
    private Product product;
    private int productQuantity;
    //if true then can't be higher than false can't be lower than
    boolean cantbemore;

    public ValidateProductQuantityDiscount() {
    }

    public ValidateProductQuantityDiscount(Shop shop, int ID, Product product, int productQuantity, boolean cantbemore) {
        super(shop, ID);
        this.product = product;
        this.productQuantity = productQuantity;
        this.cantbemore = cantbemore;
    }

    public ValidateProductQuantityDiscount(Product product, int productQuantity, boolean cantbemore) {
        this.product = product;
        this.productQuantity = productQuantity;
        this.cantbemore = cantbemore;
    }

    public Product getProduct() {
        return product;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public boolean isCantbemore() {
        return cantbemore;
    }
}
