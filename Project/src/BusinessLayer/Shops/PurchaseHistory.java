package BusinessLayer.Shops;

import java.util.ArrayList;
import java.util.Collection;

public class PurchaseHistory {
    int shopid;
    String user;
    Collection<Purchase> past_purchases;
    public PurchaseHistory(int shopid, String user)
    {
        this.shopid = shopid;
        this.user= user;
        this.past_purchases = new ArrayList<>();
    }

    public void makePurchase()
    {
        Purchase purchase = new Purchase(shopid, user, past_purchases.size(),ShopController.getInstance().getShops().get(shopid).getUsersBaskets().get(user));
        past_purchases.add(purchase);
    }

    public int getShopid() {
        return shopid;
    }

    public String getUser() {
        return user;
    }

    public Collection<Purchase> getPast_purchases() {
        return past_purchases;
    }
}
