package BusinessLayer.Shops;


import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ShopController{


    static private class ShopControllerHolder {
        static final ShopController sc = new ShopController();
    }

    public static ShopController getInstance(){
        return ShopControllerHolder.sc;
    }


    private final Map<Integer, Shop> shops;

    public ShopController() {
        this.shops = new ConcurrentHashMap<>();
    }

    public Map<Shop,Collection<Product>> searchProducts(ShopFilters shopPred, ProductFilters productPred){
        Map<Shop,Collection<Product>> res = new ConcurrentHashMap<>();
        for (Shop s : shops.values().stream().filter(shopPred).collect(Collectors.toSet())) {
            res.put(s,s.searchProducts(productPred));
        }
        return res;
    }

    public ConcurrentHashMap<Integer,Double> purchaseBasket(ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Integer>> shoppingCart)
    {
        ConcurrentHashMap<Integer,Double> finalprices= new ConcurrentHashMap<>();
        for (int shopid: shoppingCart.keySet()) {
            if (shops.containsKey(shopid)) {
                finalprices.put(shopid, shops.get(shopid).purchaseBasket(shoppingCart.get(shopid)));
            }
        }
        return finalprices;
    }

    public boolean addToPurchaseHistory(ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Integer>>cart, ConcurrentHashMap<Integer,Boolean> paymentSituation)
    {
        return true;
    }

}
