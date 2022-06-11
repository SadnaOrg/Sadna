package ServiceLayer.Objects.Policies.Discount;

import BusinessLayer.Shops.Polices.Discount.DiscountRules;

import java.util.ArrayList;
import java.util.Collection;

public record DiscountMaxPolicy(int connectId, Collection<ServiceLayer.Objects.Policies.Discount.DiscountRules> discountPolicies)implements ServiceLayer.Objects.Policies.Discount.DiscountRules {
    public DiscountMaxPolicy(BusinessLayer.Shops.Polices.Discount.DiscountMaxPolicy discountMaxPolicy)
    {
        this(discountMaxPolicy.getConnectId() ,new ArrayList<>());
        for (BusinessLayer.Shops.Polices.Discount.DiscountRules discountRules:discountMaxPolicy.getDiscountPolicies())
        {
            discountPolicies.add(ServiceLayer.Objects.Policies.Discount.DiscountRules.makeServiceRule(discountRules));
        }
    }
}
