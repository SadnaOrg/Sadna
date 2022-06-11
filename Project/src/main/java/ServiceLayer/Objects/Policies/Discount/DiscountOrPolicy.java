package ServiceLayer.Objects.Policies.Discount;

import BusinessLayer.Shops.Polices.Discount.DiscountPred;

import java.util.ArrayList;
import java.util.Collection;

public record DiscountOrPolicy(Collection<ServiceLayer.Objects.Policies.Discount.DiscountPred> discountPreds, DiscountRules discountPolicy) implements ServiceLayer.Objects.Policies.Discount.DiscountRules {

    public DiscountOrPolicy(BusinessLayer.Shops.Polices.Discount.DiscountOrPolicy discountOrPolicy)
    {
        this(ServiceLayer.Objects.Policies.Discount.DiscountRules.makeServiceRule(discountOrPolicy.getDiscountPolicy()));
        for (DiscountPred discountPred:discountOrPolicy.getDiscountPreds())
        {
            discountPreds.add(ServiceLayer.Objects.Policies.Discount.DiscountPred.makeServicePred(discountPred));
        }
    }
    public DiscountOrPolicy(ServiceLayer.Objects.Policies.Discount.DiscountRules discountPolicy)
    {
        this(new ArrayList<>(),discountPolicy);
    }
}
