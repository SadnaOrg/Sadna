package com.SadnaORM.ShopImpl.ShopObjects.Policies;

public class ValidateProductPurchaseDTO extends PurchasePolicyDTO{
    private int productId;
    private int productQuantity;
    //if true then can't be higher than false can't be lower than
    private boolean cantbemore;

    public ValidateProductPurchaseDTO(int ID, int shopID, int productId, int productQuantity, boolean cantbemore) {
        super(ID, shopID);
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.cantbemore = cantbemore;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public boolean isCantbemore() {
        return cantbemore;
    }

    public void setCantbemore(boolean cantbemore) {
        this.cantbemore = cantbemore;
    }
}
