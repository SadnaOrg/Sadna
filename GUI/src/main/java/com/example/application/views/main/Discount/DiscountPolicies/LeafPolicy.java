package com.example.application.views.main.Discount.DiscountPolicies;

import ServiceLayer.interfaces.SubscribedUserService;

public class LeafPolicy extends DiscountPolicy{
    public LeafPolicy(SubscribedUserService service, int shopId, int parentId) {
        super(service, shopId, parentId);
    }
}
