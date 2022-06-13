package com.example.application.views.main.Discount.DiscountPolicies;

import ServiceLayer.interfaces.SubscribedUserService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class DiscountPolicy {
    protected SubscribedUserService service;
    protected int shopId;
    protected int parentId;
    protected VerticalLayout layout;

    public DiscountPolicy(SubscribedUserService service, int shopId, int parentId) {
        layout = new VerticalLayout();
        this.service = service;
        this.shopId = shopId;
        this.parentId = parentId;
    }

    public VerticalLayout getLayout() {
        return layout;
    }
}
