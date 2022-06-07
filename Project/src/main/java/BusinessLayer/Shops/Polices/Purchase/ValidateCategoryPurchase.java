package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

import java.util.Collection;

public class ValidateCategoryPurchase implements PurchasePolicy{

    //category
    Collection<Integer> relatedProducts;
    int productQuantity;
    //if true then can't be higher than false can't be lower than
    boolean cantbemore;

    public boolean isValid(User u, Basket basket) {

        boolean valid =true;
        for (int pid:basket.getProducts().keySet()) {
            if(relatedProducts.contains(pid))
            {
                if(cantbemore)
                    valid= valid && basket.getProducts().get(pid)<=productQuantity;
                else
                    valid= valid && basket.getProducts().get(pid)>=productQuantity;
            }
        }
      return valid;
    }
}
