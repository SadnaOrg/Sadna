package ServiceLayer.Objects.Policies.Purchase;

public record ValidateUserPurchase(int policyLogicId, int age) implements PurchasePolicy {

    public ValidateUserPurchase(BusinessLayer.Shops.Polices.Purchase.ValidateUserPurchase validateUserPurchase)
    {
        this(validateUserPurchase.getPolicyLogicId(), validateUserPurchase.getAge());
    }

}
