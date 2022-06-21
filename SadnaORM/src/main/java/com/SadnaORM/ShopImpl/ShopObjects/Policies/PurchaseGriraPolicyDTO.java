package com.SadnaORM.ShopImpl.ShopObjects.Policies;

public class PurchaseGriraPolicyDTO extends PurchasePolicyDTO{
    private PurchasePolicyDTO purchasePolicyAllow;
    private PurchasePolicyDTO validatePurchase;

    public PurchaseGriraPolicyDTO(int ID, int shopID, PurchasePolicyDTO purchasePolicyAllow, PurchasePolicyDTO validatePurchase) {
        super(ID, shopID);
        this.purchasePolicyAllow = purchasePolicyAllow;
        this.validatePurchase = validatePurchase;
    }

    public PurchasePolicyDTO getPurchasePolicyAllow() {
        return purchasePolicyAllow;
    }

    public void setPurchasePolicyAllow(PurchasePolicyDTO purchasePolicyAllow) {
        this.purchasePolicyAllow = purchasePolicyAllow;
    }

    public PurchasePolicyDTO getValidatePurchase() {
        return validatePurchase;
    }

    public void setValidatePurchase(PurchasePolicyDTO validatePurchase) {
        this.validatePurchase = validatePurchase;
    }
}
