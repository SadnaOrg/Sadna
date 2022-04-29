package BusinessLayer.Users;

import java.util.HashMap;

public abstract class User {

    //the key is the shopid
    //the value is the basket of the specific shop
    private HashMap<Integer, Basket> shoppingCart;
    //assume that the productid is in the relevant shop handle in facade
    public boolean saveProducts(int shopid, int productid, int quantity)
    {
        if(!shoppingCart.containsKey(shopid))
        {
            Basket b = new Basket();
            shoppingCart.put(shopid,b);
        }
        Basket b = shoppingCart.get(shopid);
        if(!b.saveProducts(productid, quantity))
        {
            //the product is already exist in the basket
            return false;
        }
        return true;
    }

}
