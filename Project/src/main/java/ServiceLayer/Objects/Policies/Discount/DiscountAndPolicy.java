package ServiceLayer.Objects.Policies.Discount;

import BusinessLayer.Shops.Polices.Discount.DiscountPred;

import java.util.ArrayList;
import java.util.Collection;

public record DiscountAndPolicy(Collection<ServiceLayer.Objects.Policies.Discount.DiscountPred> discountPreds, ServiceLayer.Objects.Policies.Discount.DiscountRules discountPolicy) implements ServiceLayer.Objects.Policies.Discount.DiscountRules {

    public DiscountAndPolicy(BusinessLayer.Shops.Polices.Discount.DiscountAndPolicy discountAndPolicy)
    {
        Collection<ServiceLayer.Objects.Policies.Discount.DiscountPred> discountPreds = new ArrayList<>();
        for (DiscountPred discountPred:discountAndPolicy.getDiscountPreds())
        {
            discountPreds.add(ServiceLayer.Objects.Policies.Discount.DiscountPred.makeServicePred(discountPred));
        }
        this(discountPreds,);
    }
}
