package com.example.application.views.main.Purchase.PurchasePolicies;

import ServiceLayer.interfaces.SubscribedUserService;

public abstract class LeafPurchase extends PurchasePolicy {
    public LeafPurchase(SubscribedUserService service, int shopId, int parentId) {
        super(service, shopId, parentId);
    }
    abstract void createValidateView();
}