package AcceptanceTests.DataObjects;

import ServiceLayer.Objects.Basket;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {

    public Map<Integer,ShopBasket> baskets;
    public ShoppingCart(){
        this.baskets = new HashMap<>();
    }

    public ShoppingCart(ServiceLayer.Objects.Cart cart){
        this.baskets = new HashMap<>();
        Collection<Basket> baskets = cart.baskets();
        for (Basket b:
             baskets) {
            this.baskets.put(b.shopId(),new ShopBasket(b));
        }
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
