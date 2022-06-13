package BusinessLayer.Users;

import BusinessLayer.Shops.ShopInfo;
import BusinessLayer.System.PaymentMethod;

import java.util.List;
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
    public boolean saveProducts(int shopid, int productid, int quantity,double price,String category) {
        if(quantity<=0)
            throw new IllegalArgumentException("quantity must be positive amount");
        if (!shoppingCart.containsKey(shopid)) {
            Basket b = new Basket(shopid);
            shoppingCart.put(shopid, b);
        }
        Basket b = shoppingCart.get(shopid);

        //the product is already exist in the basket
        return b.saveProducts(productid, quantity,price,category);
    }

    public ConcurrentHashMap<Integer,Integer> getProducts(int shopid){
        if(shoppingCart.containsKey(shopid))
            return shoppingCart.get(shopid).getProducts();
        return null;
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
            shoppingCart.get(shopid).removeProduct(productid);
            Basket b = getBasket(shopid);
            if(b.getProducts().size() == 0)
                shoppingCart.remove(shopid);
            return true;
        }
        return false;
    }


    public boolean editProductQuantity(int shopid, int productid, int newquantity){
        if(newquantity==0)
            return removeProduct(shopid,productid);
        if(newquantity < 0)
            throw new IllegalArgumentException("can't have a product with a negative quantity");
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
        return shoppingCart.getOrDefault(shopid,null);
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

    public void removeBaskets(List<Integer> IDs) {
        for (Integer id:
             IDs) {
            Basket b = getBasket(id);
            if(b == null)
                throw new IllegalArgumentException("you don't have a basket in that shop!");
            shoppingCart.remove(id);
        }
    }
}
