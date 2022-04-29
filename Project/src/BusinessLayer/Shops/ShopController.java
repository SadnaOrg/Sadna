package BusinessLayer.Shops;


import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ShopController{
    private final Map<String, Shop> shops;

    public ShopController() {
        this.shops = new ConcurrentHashMap<>();
    }

    public Map<Shop,Collection<Product>> searchProducts(ShopFilters shopPred,ProductFilters productPred){
        Map<Shop,Collection<Product>> res = new ConcurrentHashMap<>();
        for (Shop s : shops.values().stream().filter(shopPred).collect(Collectors.toSet())) {
            res.put(s,s.searchProducts(productPred));
        }
        return res;
    }

}
