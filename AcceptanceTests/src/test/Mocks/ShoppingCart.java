package test.Mocks;

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

    public int getShop(int shopNumber) {
        return shopsList.get(shopNumber);
    }

    public int getProduct(int productNumber) {
        return productList.get(productNumber);
    }

    public int getQuantity(int quantityNumber) {
        return quantities.get(quantityNumber);
    }
}
