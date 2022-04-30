package BusinessLayer.Users;

import java.util.HashMap;

public abstract class User {

    //the key is the shopid
    //the value is the basket of the specific shop
    private HashMap<Integer, Basket> shoppingCart = new HashMap<>();


    //assume that the productid is in the relevant shop handle in facade
    public boolean saveProducts(int shopid, int productid, int quantity) {
        if (!shoppingCart.containsKey(shopid)) {
            Basket b = new Basket();
            shoppingCart.put(shopid, b);
        }
        Basket b = shoppingCart.get(shopid);

        if (!b.saveProducts(productid, quantity)) {
            //the product is already exist in the basket
            return false;
        }
        return true;
    }

    public boolean search_in_shopping_cart()
    {
        for (Basket b:shoppingCart.values())
        {
            //print each basket in specific format
        }
        //ask for a specific shopid from the user
        int shopid= 1;//change to what user want
        //show options and let him choose
        return true;
    }


    public boolean removeproduct(int shopid, int productid)
    {
        if(shoppingCart.containsKey(shopid)) {
            return shoppingCart.get(shopid).removeProduct(productid);
        }
        return false;
    }


    public boolean editProductQuantity(int shopid, int productid, int newquantity)
    {
        if(shoppingCart.containsKey(shopid)) {
            return shoppingCart.get(shopid).editProductQuantity(productid, newquantity);
        }
        return false;
    }

    public HashMap<Integer, Basket> getShoppingCart() {
        return shoppingCart;
    }
}
