package ServiceLayer.Objects;

import BusinessLayer.Users.BasketInfo;

import java.util.Collection;
import java.util.LinkedList;

public record Basket(Collection<ProductInfo> productsID,int shopId) {
    public Basket(BasketInfo b) {
        this(b.getShopid());
        for (int i :b.getProducts().keySet()) {
            productsID.add(new ProductInfo(shopId,i,b.getProducts().get(i),b.getPrices().get(i)));
        }

    }

    public Basket(int shopid) {
        this(new LinkedList<>(),shopid);
    }
}
