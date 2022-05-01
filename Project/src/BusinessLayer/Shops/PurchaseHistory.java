package BusinessLayer.Shops;

import java.util.ArrayList;
import java.util.Collection;

public class PurchaseHistory {
    Shop shop;
    String user;
    Collection<Purchase> past_purchases;
    public PurchaseHistory(Shop shop, String user)
    {
        this.shop = shop;
        this.user= user;
        this.past_purchases = new ArrayList<>();
    }

    public void makePurchase()
    {
        Purchase purchase = new Purchase(shop.getId(), user, past_purchases.size()+1,shop.getUsersBaskets().get(user));
        past_purchases.add(purchase);
    }

    public Shop getShop() {
        return shop;
    }

    public String getUser() {
        return user;
    }

    public Collection<Purchase> getPast_purchases() {
        return past_purchases;
    }

}
