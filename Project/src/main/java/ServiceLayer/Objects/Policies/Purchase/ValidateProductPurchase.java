package ServiceLayer.Objects.Policies.Purchase;

public record ValidateProductPurchase(int policyLogicId, int productId, int productQuantity, boolean cantbemore) implements PurchasePolicy{

    public ValidateProductPurchase(BusinessLayer.Shops.Polices.Purchase.ValidateProductPurchase validateProductPurchase)
    {
        this(validateProductPurchase.getPolicyLogicId(), validateProductPurchase.getProductId(), validateProductPurchase.getProductQuantity()
                , validateProductPurchase.isCantbemore());
    }

}
