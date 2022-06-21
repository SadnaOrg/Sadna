package AcceptanceTests.DataObjects;

import java.util.List;

public class PurchaseHistory {
    public Shop shop;
    public String user;
    public List<Purchase> purchases;

    public PurchaseHistory(ServiceLayer.Objects.PurchaseHistory purchaseHistory){
        this.shop = new Shop(purchaseHistory.shop());
        this.user = purchaseHistory.user();
        this.purchases = purchaseHistory.purchases().stream().map(Purchase::new).toList();
    }
}
