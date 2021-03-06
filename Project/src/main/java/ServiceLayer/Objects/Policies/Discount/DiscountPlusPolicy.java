package ServiceLayer.Objects.Policies.Discount;

import java.util.ArrayList;
import java.util.Collection;

public record DiscountPlusPolicy(int connectId, Collection<ServiceLayer.Objects.Policies.Discount.DiscountRules> discountPolicies)implements ServiceLayer.Objects.Policies.Discount.DiscountRules {

    public DiscountPlusPolicy(BusinessLayer.Shops.Polices.Discount.DiscountPlusPolicy discountMaxPolicy)
    {
        this(discountMaxPolicy.getConnectId(),new ArrayList<>());
        for (BusinessLayer.Shops.Polices.Discount.DiscountRules discountRules:discountMaxPolicy.getDiscountPolicies())
        {
            discountPolicies.add(ServiceLayer.Objects.Policies.Discount.DiscountRules.makeServiceRule(discountRules));
        }
    }

}
