package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

import java.util.Collection;

public class PurchaseGriraPolicy implements PurchasePolicy{


    private PurchasePolicy purchasePolicyAllow;
    private ValidateCategoryPurchase validateCategoryPurchase;

    public PurchaseGriraPolicy(PurchasePolicy purchasePolicyAllow, ValidateCategoryPurchase validateCategoryPurchase) {
        this.purchasePolicyAllow = purchasePolicyAllow;
        this.validateCategoryPurchase = validateCategoryPurchase;
    }

    @Override
    public boolean isValid(User u, Basket basket) {
        if(purchasePolicyAllow.isValid(u,basket))
        {
            return validateCategoryPurchase.isValid(u,basket);
        }
        return false;
    }
}
