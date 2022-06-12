package ServiceLayer.Objects.Policies.Discount;

import java.util.ArrayList;
import java.util.Collection;

public interface DiscountRules{

    static BusinessLayer.Shops.Polices.Discount.DiscountRules makeBusinessRule(DiscountRules discountRules) {

        if(discountRules instanceof ProductByQuantityDiscount)
        {
            return new BusinessLayer.Shops.Polices.Discount.ProductByQuantityDiscount
                    (((ProductByQuantityDiscount) discountRules).discountId(),
                            ((ProductByQuantityDiscount) discountRules).productId(),
                            ((ProductByQuantityDiscount) discountRules).productQuantity(),
                            ((ProductByQuantityDiscount) discountRules).discount());
        }

        if(discountRules instanceof ProductDiscount)
        {
            return new BusinessLayer.Shops.Polices.Discount.ProductDiscount
                    (((ProductDiscount) discountRules).discountId(), ((ProductDiscount) discountRules).productId(),
                            ((ProductDiscount) discountRules).discount());
        }

        if(discountRules instanceof ProductQuantityInPriceDiscount)
        {
            return new BusinessLayer.Shops.Polices.Discount.ProductQuantityInPriceDiscount
                    (((ProductQuantityInPriceDiscount) discountRules).discountId(),
                            ((ProductQuantityInPriceDiscount) discountRules).productID(),
                            ((ProductQuantityInPriceDiscount) discountRules).quantity(),
                            ((ProductQuantityInPriceDiscount) discountRules).priceForQuantity());
        }

        if(discountRules instanceof ShopDiscount)
        {
            return new BusinessLayer.Shops.Polices.Discount.ShopDiscount
                    (((ShopDiscount) discountRules).discountId(), ((ShopDiscount) discountRules).basketQuantity(),
                            ((ShopDiscount) discountRules).discount());
        }

        if(discountRules instanceof DiscountAndPolicy)
        {
            Collection<BusinessLayer.Shops.Polices.Discount.DiscountPred> preds = new ArrayList<>();
            for (DiscountPred discountPred: ((DiscountAndPolicy) discountRules).discountPreds())
            {
                preds.add(DiscountPred.makeBusinessPred(discountPred));
            }

            return new BusinessLayer.Shops.Polices.Discount.DiscountAndPolicy
                    ( ((DiscountAndPolicy) discountRules).connectId(),
                            preds,
                            (DiscountRules.makeBusinessRule(((DiscountAndPolicy) discountRules).discountPolicy())));
        }

        if(discountRules instanceof DiscountOrPolicy)
        {
            Collection<BusinessLayer.Shops.Polices.Discount.DiscountPred> preds = new ArrayList<>();
            for (DiscountPred discountPred: ((DiscountOrPolicy) discountRules).discountPreds())
            {
                preds.add(DiscountPred.makeBusinessPred(discountPred));
            }

            return new BusinessLayer.Shops.Polices.Discount.DiscountOrPolicy
                    ( ((DiscountOrPolicy) discountRules).connectId(),
                            (DiscountRules.makeBusinessRule(((DiscountOrPolicy) discountRules).discountPolicy())),
                            preds);
        }

        if(discountRules instanceof DiscountXorPolicy)
        {
            Collection<BusinessLayer.Shops.Polices.Discount.DiscountPred> preds = new ArrayList<>();
            for (DiscountPred discountPred: ((DiscountXorPolicy) discountRules).tieBreakers())
            {
                preds.add(DiscountPred.makeBusinessPred(discountPred));
            }

            return new BusinessLayer.Shops.Polices.Discount.DiscountXorPolicy
                    ( ((DiscountXorPolicy) discountRules).connectId(),
                     (DiscountRules.makeBusinessRule(((DiscountXorPolicy) discountRules).discountRules1())),
                      (DiscountRules.makeBusinessRule(((DiscountXorPolicy) discountRules).discountRules2())),
                            preds);
        }

        if(discountRules instanceof DiscountMaxPolicy)
        {
            Collection<BusinessLayer.Shops.Polices.Discount.DiscountRules> rules = new ArrayList<>();
            for (DiscountRules discountRule: ((DiscountMaxPolicy) discountRules).discountPolicies())
            {
                rules.add(DiscountRules.makeBusinessRule(discountRule));
            }

            return new BusinessLayer.Shops.Polices.Discount.DiscountMaxPolicy
                    ( ((DiscountMaxPolicy) discountRules).connectId(),
                            rules);
        }

        if(discountRules instanceof DiscountPlusPolicy)
        {
            Collection<BusinessLayer.Shops.Polices.Discount.DiscountRules> rules = new ArrayList<>();
            for (DiscountRules discountRule: ((DiscountPlusPolicy) discountRules).discountPolicies())
            {
                rules.add(DiscountRules.makeBusinessRule(discountRule));
            }

            return new BusinessLayer.Shops.Polices.Discount.DiscountPlusPolicy
                    ( ((DiscountPlusPolicy) discountRules).connectId(),
                            rules);
        }


        throw new IllegalStateException("can't be other then the known ones");
    }

    static DiscountRules makeServiceRule(BusinessLayer.Shops.Polices.Discount.DiscountRules discountRules)
    {
        if(discountRules instanceof BusinessLayer.Shops.Polices.Discount.ProductByQuantityDiscount)
        {
            return new ProductByQuantityDiscount((BusinessLayer.Shops.Polices.Discount.ProductByQuantityDiscount)discountRules);
        }
        if(discountRules instanceof BusinessLayer.Shops.Polices.Discount.ProductDiscount)
        {
            return new ProductDiscount((BusinessLayer.Shops.Polices.Discount.ProductDiscount)discountRules);
        }
        if(discountRules instanceof BusinessLayer.Shops.Polices.Discount.ProductQuantityInPriceDiscount)
        {
            return new ProductQuantityInPriceDiscount((BusinessLayer.Shops.Polices.Discount.ProductQuantityInPriceDiscount)discountRules);
        }
        if(discountRules instanceof BusinessLayer.Shops.Polices.Discount.ShopDiscount)
        {
            return new ShopDiscount((BusinessLayer.Shops.Polices.Discount.ShopDiscount)discountRules);
        }
        if(discountRules instanceof BusinessLayer.Shops.Polices.Discount.DiscountAndPolicy)
        {
            return new DiscountAndPolicy((BusinessLayer.Shops.Polices.Discount.DiscountAndPolicy)discountRules);
        }
        if(discountRules instanceof BusinessLayer.Shops.Polices.Discount.DiscountOrPolicy)
        {
            return new DiscountOrPolicy((BusinessLayer.Shops.Polices.Discount.DiscountOrPolicy)discountRules);
        }
        if(discountRules instanceof BusinessLayer.Shops.Polices.Discount.DiscountXorPolicy)
        {
            return new DiscountXorPolicy((BusinessLayer.Shops.Polices.Discount.DiscountXorPolicy)discountRules);
        }
        if(discountRules instanceof BusinessLayer.Shops.Polices.Discount.DiscountMaxPolicy)
        {
            return new DiscountMaxPolicy((BusinessLayer.Shops.Polices.Discount.DiscountMaxPolicy)discountRules);
        }
        if(discountRules instanceof BusinessLayer.Shops.Polices.Discount.DiscountPlusPolicy)
        {
            return new DiscountPlusPolicy((BusinessLayer.Shops.Polices.Discount.DiscountPlusPolicy)discountRules);
        }
        throw new IllegalStateException("can't be other then the known ones");
    }
}
