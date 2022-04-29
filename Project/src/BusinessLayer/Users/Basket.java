package BusinessLayer.Users;

import java.util.concurrent.ConcurrentHashMap;

public class Basket {
    private int shopid;
    //the key is the product id in the specific store
    //the value will be the quantity of the product
    private ConcurrentHashMap<Integer , Integer> products = new ConcurrentHashMap<>();

    //secret
    public boolean saveProducts(int productid, int quantity) {
        if(!products.containsKey(productid))
        {
            products.put(productid,quantity);
            return true;
        }
        return false;
    }


    public boolean removeProduct(int productid) {
        if (products.containsKey(productid)) {
            products.remove(productid);
            return true;
        }
        return false;
    }

    public boolean editProductQuantity(int productid, int newquantity) {
        if (products.containsKey(productid)) {
            products.put(productid, newquantity);
            return true;
        }
        return false;
    }
    public ConcurrentHashMap<Integer, Integer> getProducts() {
        return products;
    }


}
