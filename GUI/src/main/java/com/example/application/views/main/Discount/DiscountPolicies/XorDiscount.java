package com.example.application.views.main.Discount.DiscountPolicies;

import ServiceLayer.interfaces.SubscribedUserService;
import com.example.application.views.main.Discount.DiscountPred.DiscountPred;

import java.util.Collection;

public class XorDiscount extends CompositePolicy{
    public XorDiscount(SubscribedUserService service, int shopId, int parentId, Collection<DiscountPred> discountPreds, DiscountPolicy discount1, DiscountPolicy discount2) {
        super(service, shopId, parentId);
        DiscountPred discountPred = new DiscountPred(service, shopId, parentId);
        policies.add(discountPred.toString(), discountPred.getLayout());
        for(DiscountPred policy : discountPreds){
            policies.add(policy.toString(), policy.getLayout());
        }
        if(discount1 != null)
            policies.add(discount1.toString(), discount1.layout);
        if(discount2 != null)
            policies.add(discount2.toString(), discount2.layout);
    }

    @Override
    public String toString() {
        return "Xor Discount";
    }
}
