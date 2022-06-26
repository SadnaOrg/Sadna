package ServiceLayer.Objects.Policies.Discount;

import BusinessLayer.Shops.Polices.Discount.DiscountPred;

import java.util.ArrayList;
import java.util.Collection;

public record DiscountOrPolicy(int connectId, Collection<ServiceLayer.Objects.Policies.Discount.DiscountPred> discountPreds, DiscountRules discountPolicy) implements ServiceLayer.Objects.Policies.Discount.DiscountRules {

    public DiscountOrPolicy( BusinessLayer.Shops.Polices.Discount.DiscountOrPolicy discountOrPolicy)
    {
        this(discountOrPolicy.getConnectId(), ServiceLayer.Objects.Policies.Discount.DiscountRules.makeServiceRule(discountOrPolicy.getDiscountPolicy()));
        for (DiscountPred discountPred:discountOrPolicy.getDiscountPreds())
        {
            discountPreds.add(ServiceLayer.Objects.Policies.Discount.DiscountPred.makeServicePred(discountPred));
        }
    }
    public DiscountOrPolicy(int connectId, ServiceLayer.Objects.Policies.Discount.DiscountRules discountPolicy)
    {
        this(connectId,new ArrayList<>(),discountPolicy);
    }
}
