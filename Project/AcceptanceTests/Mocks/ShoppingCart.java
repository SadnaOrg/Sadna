package Mocks;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {

    public Map<Integer,ShopBasket> baskets;
    public ShoppingCart(){
        this.baskets = new HashMap<>();
    }

    public int numOfProductsInCart() {
        int num = 0;
        for (ShopBasket b:
             baskets.values()) {
            num += b.numOfProducts();
        }
        return num;
    }

    public ShopBasket getShopBasket(int shopID) {
        return baskets.getOrDefault(shopID,null);
    }

    public int getNumberOfBaskets(){return baskets.size();}
}
