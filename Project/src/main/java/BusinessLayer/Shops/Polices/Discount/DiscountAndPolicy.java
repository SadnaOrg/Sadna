package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.Collection;

public class DiscountAndPolicy implements LogicDiscountRules{
     private Collection<DiscountPred> discountPreds;


     @Override
     public boolean validateDiscount(Basket basket){
         for(DiscountPred discountPred: discountPreds)
         {
             if(!discountPred.validateDiscount(basket))
                 return false;
         }
         return true;
     }
}
