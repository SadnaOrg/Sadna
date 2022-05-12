package BusinessLayer.Users;

import BusinessLayer.Shops.ShopInfo;

import BusinessLayer.Shops.ShopController;
import BusinessLayer.System.PaymentMethod;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public abstract class User{

    //the key is the shopid
    //the value is the basket of the specific shop
    private ConcurrentHashMap<Integer, Basket> shoppingCart;
    protected String name;
    protected PaymentMethod method;

    public User(String name) {
        this.name= name;
        method = null;
        shoppingCart= new ConcurrentHashMap<>();
    }

    //assume that the productid is in the relevant shop handle in facade
    public boolean saveProducts(int shopid, int productid, int quantity) {
        if (!shoppingCart.containsKey(shopid)) {
            Basket b = new Basket(shopid);
            shoppingCart.put(shopid, b);
        }
        Basket b = shoppingCart.get(shopid);

        //the product is already exist in the basket
        return b.saveProducts(productid, quantity);
    }

    public ConcurrentHashMap<Integer,Integer> getProducts(int shopid){
        return shoppingCart.get(shopid).getProducts();
    }

    public ConcurrentHashMap<Integer,BasketInfo> showCart(){
        ConcurrentHashMap<Integer,BasketInfo> cartInfo = new ConcurrentHashMap<>();
        for (Basket b:shoppingCart.values())
        {
            cartInfo.put(b.getShopid(),new BasketInfo(b));
        }
        return cartInfo;
    }

    public boolean removeProduct(int shopid, int productid){
        if(shoppingCart.containsKey(shopid)) {
            return shoppingCart.get(shopid).removeProduct(productid);
        }
        return false;
    }


    public boolean editProductQuantity(int shopid, int productid, int newquantity){
        if(shoppingCart.containsKey(shopid)) {
            return shoppingCart.get(shopid).editProductQuantity(productid, newquantity);
        }
        return false;
    }

    public ConcurrentHashMap<Integer, Basket> getShoppingCart() {
        return shoppingCart;
    }
    public ConcurrentHashMap<Integer, ShopInfo> receiveInformation()
    {
        return UserController.getInstance().reciveInformation();
    }

    public Basket getBasket(int shopid)
    {
        return shoppingCart.get(shopid);
    }

    public synchronized void updatePaymentMethod(PaymentMethod method){
        this.method = method;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public String getName() {
        return name;
    }
    public String getUserName(){return name;}

    public boolean isLoggedIn(){
        return true;
    }
}
