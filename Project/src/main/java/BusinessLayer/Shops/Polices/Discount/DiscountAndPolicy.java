package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.ArrayList;
import java.util.Collection;

public class DiscountAndPolicy implements LogicDiscountRules{
     private Collection<DiscountPred> discountPreds;

    public DiscountAndPolicy(Collection<DiscountPred> discountPreds) {
        this.discountPreds = new ArrayList<>();
        this.discountPreds.addAll(discountPreds);
    }

    public DiscountAndPolicy(DiscountPred discountPred) {
        this.discountPreds = new ArrayList<>();
        this.discountPreds.add(discountPred);
    }



    @Override
     public boolean validateDiscount(Basket basket){
         for(DiscountPred discountPred: discountPreds)
         {
             if(!discountPred.validateDiscount(basket))
                 return false;
         }
         return true;
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
