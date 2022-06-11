package ServiceLayer.Objects.Policies.Discount;



import java.util.ArrayList;
import java.util.Collection;

public record DiscountXorPolicy(int connectId, DiscountRules discountRules1, DiscountRules discountRules2, Collection<DiscountPred> tieBreakers) implements ServiceLayer.Objects.Policies.Discount.DiscountRules {

    public DiscountXorPolicy(BusinessLayer.Shops.Polices.Discount.DiscountXorPolicy discountXorPolicy)
    {
        this(discountXorPolicy.getConnectId(), ServiceLayer.Objects.Policies.Discount.DiscountRules.makeServiceRule(discountXorPolicy.getDiscountRules1())
        ,ServiceLayer.Objects.Policies.Discount.DiscountRules.makeServiceRule(discountXorPolicy.getDiscountRules2()));
        for (BusinessLayer.Shops.Polices.Discount.DiscountPred discountPred:discountXorPolicy.getTieBreakers())
        {
            tieBreakers.add(ServiceLayer.Objects.Policies.Discount.DiscountPred.makeServicePred(discountPred));
        }
    }

    public DiscountXorPolicy(int connectId, DiscountRules makeServiceRule, DiscountRules makeServiceRule1) {
        this(connectId,makeServiceRule,makeServiceRule1,new ArrayList<>());

    }
}
