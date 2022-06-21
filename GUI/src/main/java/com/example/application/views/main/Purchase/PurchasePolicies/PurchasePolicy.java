package com.example.application.views.main.Purchase.PurchasePolicies;

import ServiceLayer.interfaces.SubscribedUserService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class PurchasePolicy {
    protected SubscribedUserService service;
    protected int shopId;
    protected int parentId;
    protected VerticalLayout layout;

    public PurchasePolicy(SubscribedUserService service, int shopId, int parentId) {
        layout = new VerticalLayout();
        this.service = service;
        this.shopId = shopId;
        this.parentId = parentId;
    }

    public VerticalLayout getLayout() {
        return layout;
    }
}
