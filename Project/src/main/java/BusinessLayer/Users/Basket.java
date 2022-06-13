package BusinessLayer.Users;
import java.util.concurrent.ConcurrentHashMap;

public class Basket {

    private int shopid;
    //the key is the product id in the specific store
    //the value will be the quantity of the product
    private ConcurrentHashMap<Integer , Integer> products = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Double> prices;
    private ConcurrentHashMap<Integer, String> categories;

    public Basket(int shopid)
    {
        this.shopid= shopid;
        products = new ConcurrentHashMap<>();
        prices = new ConcurrentHashMap<>();
        categories = new ConcurrentHashMap<>();
    }

    public Basket(Basket bmain)
    {
        this.shopid =bmain.shopid;
        this.products = new ConcurrentHashMap<>();
        this.prices = new ConcurrentHashMap<>();
        this.categories = new ConcurrentHashMap<>();
        for (int pid:bmain.getProducts().keySet())
        {
            products.put(pid,bmain.getProducts().get(pid));
            prices.put(pid,bmain.getPrices().get(pid));
            categories.put(pid,bmain.getCategories().get(pid));
        }
    }

    public boolean saveProducts(int productid, int quantity,double price,String category) {
       if(!products.containsKey(productid))
        {
            products.put(productid,quantity);
            prices.put(productid,price);
            categories.put(productid,category);
            return true;
        }
        else
        {
            throw new IllegalStateException("The product is already in the basket");
        }
    }


    public boolean removeProduct(int productid) {
        if (products.containsKey(productid)) {
            products.remove(productid);
            prices.remove(productid);
            categories.remove(productid);
            return true;
        }
        else
        {
            throw new IllegalStateException("The product is not exist in the basket");
        }
    }

    public boolean editProductQuantity(int productid, int newquantity) {
        if(newquantity < 0){
            throw new IllegalStateException("a product can't appear in a basket with a negative quantity!");
        }
        if (products.containsKey(productid)) {
            if(newquantity == 0){
                removeProduct(productid);
            }
            else{
                products.put(productid, newquantity);
            }
            return true;
        }
        else
        {
            throw new IllegalStateException("The product is not exist in the basket");
        }
    }
    public ConcurrentHashMap<Integer, Integer> getProducts() {
        return products;
    }

    public ConcurrentHashMap<Integer, Double> getPrices(){
        return prices;
    }

    public ConcurrentHashMap<Integer, String> getCategories() {
        return categories;
    }

    public int getShopid() {
        return shopid;
    }
}
