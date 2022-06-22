package com.example.application.views.main.Purchase.PurchasePolicies;

import ServiceLayer.interfaces.SubscribedUserService;
import com.example.application.views.main.Discount.DiscountPred.DiscountPred;

import java.util.Collection;

public class AndPurchase extends CompositePurchase {
    public AndPurchase(SubscribedUserService service, int shopId, int parentId, Collection<PurchasePolicy> purchases) {
        super(service, shopId, parentId);
        for(PurchasePolicy policy : purchases) {
            policies.add(policy.toString(), policy.getLayout());
        }
    }

    @Override
    public String toString() {
        return "And Discount";
    }
}
