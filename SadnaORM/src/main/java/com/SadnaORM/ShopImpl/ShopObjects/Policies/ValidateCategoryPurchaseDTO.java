package com.SadnaORM.ShopImpl.ShopObjects.Policies;

public class ValidateCategoryPurchaseDTO extends PurchasePolicyDTO{
    private String category;
    private int productQuantity;
    //if true then can't be higher than false can't be lower than
    private boolean cantbemore;

    public ValidateCategoryPurchaseDTO(int ID, int shopID, String category, int productQuantity, boolean cantbemore) {
        super(ID, shopID);
        this.category = category;
        this.productQuantity = productQuantity;
        this.cantbemore = cantbemore;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
