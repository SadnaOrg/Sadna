package com.example.application.views.main.Discount.DiscountPolicies;

import ServiceLayer.interfaces.SubscribedUserService;
import com.example.application.views.main.Discount.DiscountPred.DiscountPred;

import java.util.Collection;

public class OrDiscount extends CompositePolicy {
    public OrDiscount(SubscribedUserService service, int shopId, int parentId, Collection<DiscountPred> discountPreds, DiscountPolicy discounts) {
        super(service, shopId, parentId);
        DiscountPred discountPred = new DiscountPred(service, shopId, parentId);
        policies.add(discountPred.toString(), discountPred.getLayout());
        for(DiscountPred policy : discountPreds){
            policies.add(policy.toString(), policy.getLayout());
        }
        if(discounts != null)
            policies.add(discounts.toString(), discounts.layout);
    }

    @Override
    public String toString() {
        return "Or Discount";
    }
}
