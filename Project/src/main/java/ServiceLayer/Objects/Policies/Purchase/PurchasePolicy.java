package ServiceLayer.Objects.Policies.Purchase;

import BusinessLayer.Shops.Polices.Purchase.ValidateTImeStampPurchase;
import ServiceLayer.Objects.Policies.Discount.ProductByQuantityDiscount;

import java.util.ArrayList;
import java.util.Collection;

public interface PurchasePolicy {

    static BusinessLayer.Shops.Polices.Purchase.PurchasePolicy makeBusinessPurchasePolicy(PurchasePolicy purchasePolicy) {
        if (purchasePolicy instanceof ValidateTimeStampPurchase) {
            return new ValidateTImeStampPurchase(((ValidateTimeStampPurchase) purchasePolicy).policyLogicId(),
                    ((ValidateTimeStampPurchase) purchasePolicy).localTime(),
                    ((ValidateTimeStampPurchase) purchasePolicy).date(),
                    ((ValidateTimeStampPurchase) purchasePolicy).buybefore()
                    );
        }
        if (purchasePolicy instanceof ValidateProductPurchase) {
            return new BusinessLayer.Shops.Polices.Purchase.ValidateProductPurchase(
                    ((ValidateProductPurchase) purchasePolicy).policyLogicId(),
                    ((ValidateProductPurchase) purchasePolicy).productId(),
                    ((ValidateProductPurchase) purchasePolicy).productQuantity(),
                    ((ValidateProductPurchase) purchasePolicy).cantbemore());
        }
        if (purchasePolicy instanceof ValidateUserPurchase) {
            return new BusinessLayer.Shops.Polices.Purchase.ValidateUserPurchase(
                    ((ValidateUserPurchase) purchasePolicy).policyLogicId(),
                    ((ValidateUserPurchase) purchasePolicy).age());
        }
        if (purchasePolicy instanceof ValidateCategoryPurchase) {
            return new BusinessLayer.Shops.Polices.Purchase.ValidateCategoryPurchase(
                    ((ValidateCategoryPurchase) purchasePolicy).policyLogicId(),
                    ((ValidateCategoryPurchase) purchasePolicy).category(),
                    ((ValidateCategoryPurchase) purchasePolicy).productQuantity(),
                    ((ValidateCategoryPurchase) purchasePolicy).cantbemore());
        }
        if (purchasePolicy instanceof PurchaseAndPolicy) {
            Collection<BusinessLayer.Shops.Polices.Purchase.PurchasePolicy> buispolicies =new ArrayList<>();
            for(PurchasePolicy policy : ((PurchaseAndPolicy) purchasePolicy).purchasePolicies()) {
                buispolicies.add(PurchasePolicy.makeBusinessPurchasePolicy(policy));
            }
            return new BusinessLayer.Shops.Polices.Purchase.PurchaseAndPolicy(
                    buispolicies,
                    ((PurchaseAndPolicy) purchasePolicy).policyLogicId());
        }
        if (purchasePolicy instanceof PurchaseOrPolicy) {
            Collection<BusinessLayer.Shops.Polices.Purchase.PurchasePolicy> buispolicies =new ArrayList<>();
            for(PurchasePolicy policy : ((PurchaseOrPolicy) purchasePolicy).purchasePolicies()) {
                buispolicies.add(PurchasePolicy.makeBusinessPurchasePolicy(policy));
            }
            return new BusinessLayer.Shops.Polices.Purchase.PurchaseOrPolicy(
                    buispolicies,
                    ((PurchaseOrPolicy) purchasePolicy).policyLogicId());
        }
        return null;
//        throw new IllegalStateException("can't be other then the known ones");
    }

    static PurchasePolicy makeServicePurchasePolicy(BusinessLayer.Shops.Polices.Purchase.PurchasePolicy purchasePolicy) {
        if (purchasePolicy instanceof BusinessLayer.Shops.Polices.Purchase.ValidateTImeStampPurchase) {
            return new ValidateTimeStampPurchase((ValidateTImeStampPurchase) purchasePolicy);
        }
        if (purchasePolicy instanceof BusinessLayer.Shops.Polices.Purchase.ValidateProductPurchase) {
            return new ValidateProductPurchase((BusinessLayer.Shops.Polices.Purchase.ValidateProductPurchase) purchasePolicy);
        }
        if (purchasePolicy instanceof BusinessLayer.Shops.Polices.Purchase.ValidateUserPurchase) {
            return new ValidateUserPurchase((BusinessLayer.Shops.Polices.Purchase.ValidateUserPurchase) purchasePolicy);
        }
        if (purchasePolicy instanceof BusinessLayer.Shops.Polices.Purchase.ValidateCategoryPurchase) {
            return new ValidateCategoryPurchase((BusinessLayer.Shops.Polices.Purchase.ValidateCategoryPurchase) purchasePolicy);
        }
        if (purchasePolicy instanceof BusinessLayer.Shops.Polices.Purchase.PurchaseAndPolicy) {
            return new PurchaseAndPolicy((BusinessLayer.Shops.Polices.Purchase.PurchaseAndPolicy) purchasePolicy);
        }
        if (purchasePolicy instanceof BusinessLayer.Shops.Polices.Purchase.PurchaseOrPolicy) {
            return new PurchaseOrPolicy((BusinessLayer.Shops.Polices.Purchase.PurchaseOrPolicy) purchasePolicy);
        }
        return null;
//        throw new IllegalStateException("can't be other then the known ones");
    }
}
