package ORM.Shops.Purchases;


import ORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ValidateProductPurchase")
public class ValidateProductPurchase extends PurchasePolicy{
    private int productId;
    private int productQuantity;
    //if true then can't be higher than false can't be lower than
    private boolean cantbemore;

    public ValidateProductPurchase() {
    }

    public ValidateProductPurchase(int productId, int productQuantity, boolean cantbemore) {
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.cantbemore = cantbemore;
    }

    public ValidateProductPurchase(Shop shop, int ID, int productId, int productQuantity, boolean cantbemore) {
        super(shop, ID);
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.cantbemore = cantbemore;
    }

    public int getProductId() {
        return productId;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public boolean isCantbemore() {
        return cantbemore;
    }
}