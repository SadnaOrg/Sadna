package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

import java.util.Collection;

public class ValidateCategoryPurchase implements ValidatePurchasePolicy{

    //category
    Collection<Integer> relatedProducts;
    int productQuantity;
    //if true then can't be higher than false can't be lower than
    boolean cantbemore;

    public ValidateCategoryPurchase(Collection<Integer> relatedProducts, int productQuantity, boolean cantbemore) {
        this.relatedProducts = relatedProducts;
        this.productQuantity = productQuantity;
        this.cantbemore = cantbemore;
    }

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
    @Override
    public LogicPurchasePolicy getLogicRule(int searchConnectId)
    {
        return null;
    }

    @Override
    public int getID(){return -1;}
}
