package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.ArrayList;
import java.util.Collection;

public class DiscountOrPolicy implements LogicDiscountRules{

    private Collection<DiscountPred> discountPreds;

    public DiscountOrPolicy(Collection<DiscountPred> discountPreds) {
        this.discountPreds = new ArrayList<>();
        this.discountPreds.addAll(discountPreds);
    }

    public DiscountOrPolicy(DiscountPred discountPred) {
        this.discountPreds = new ArrayList<>();
        this.discountPreds.add(discountPred);
    }

    @Override
    public boolean validateDiscount(Basket basket){
        for(DiscountPred discountPred: discountPreds)
        {
            if(discountPred.validateDiscount(basket))
                return true;
        }
        return false;
    }


    @Override
    public void add(DiscountPred discountPred) {
        discountPreds.add(discountPred);
    }

    @Override
    public boolean remove(DiscountPred discountPred) {
        return discountPreds.remove(discountPred);
    }

}
