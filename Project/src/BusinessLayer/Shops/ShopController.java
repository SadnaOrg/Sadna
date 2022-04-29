package BusinessLayer.Shops;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShopController{
    private final Map<String, Shop> shops;

    public ShopController() {
        this.shops = new ConcurrentHashMap<>();
    }
}
