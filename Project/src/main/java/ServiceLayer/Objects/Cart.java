package ServiceLayer.Objects;

import BusinessLayer.Products.Users.BasketInfo;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public record Cart(Collection<Basket> baskets, String user) {
     public Cart(ConcurrentHashMap<Integer, BasketInfo> cart, String user) {
         this(user);
         for (BasketInfo b: cart.values()) {
             baskets.add(new Basket(b));
         }
    }

    public Cart(String user) {
         this(new LinkedList<>(),user);
    }
}
