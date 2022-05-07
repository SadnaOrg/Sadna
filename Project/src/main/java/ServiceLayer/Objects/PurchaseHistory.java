package ServiceLayer.Objects;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public record PurchaseHistory(Shop shop, String user, List<Purchase> purchases) {
    public PurchaseHistory(BusinessLayer.Shops.PurchaseHistory h) {
        this(new Shop(h.getShop()),h.getUser(),h.getPast_purchases());
    }

    public PurchaseHistory(Shop shop, String user, Collection<BusinessLayer.Shops.Purchase> past_purchases) {
        this(shop,user,past_purchases.stream().map(Purchase::new).collect(Collectors.toList()));
    }
}
