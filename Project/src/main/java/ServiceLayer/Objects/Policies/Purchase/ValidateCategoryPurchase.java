package ServiceLayer.Objects.Policies.Purchase;

public record ValidateCategoryPurchase(int policyLogicId, String category, int productQuantity, boolean cantbemore) implements PurchasePolicy{

    public ValidateCategoryPurchase(BusinessLayer.Shops.Polices.Purchase.ValidateCategoryPurchase validateCategoryPurchase)
    {
        this(validateCategoryPurchase.getPolicyLogicId(), validateCategoryPurchase.getCategory(),
                validateCategoryPurchase.getProductQuantity(), validateCategoryPurchase.isCantbemore());
    }
}
