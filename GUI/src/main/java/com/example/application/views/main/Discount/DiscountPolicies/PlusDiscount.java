package com.example.application.views.main.Discount.DiscountPolicies;

import ServiceLayer.interfaces.SubscribedUserService;
import com.vaadin.flow.component.Component;

import java.util.Collection;

public class PlusDiscount extends CompositePolicy{
    public PlusDiscount(SubscribedUserService service, int shopId, int parentId, Collection<DiscountPolicy> discountPolicies) {
        super(service, shopId, parentId);
        for(DiscountPolicy policy : discountPolicies){
            policies.add(policy.toString(), policy.layout);
        }
    }

    @Override
    public String toString() {
        return "Plus Discount";
    }
}
