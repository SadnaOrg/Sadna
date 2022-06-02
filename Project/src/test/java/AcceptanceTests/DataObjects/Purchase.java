package AcceptanceTests.DataObjects;

import java.util.Date;
import java.util.List;

public class Purchase {
    public String user;
    public int shopId;
    public Date dateOfPurchase;
    int transactionId;
    public List<ProductInfo> products;

    public Purchase(ServiceLayer.Objects.Purchase purchase){
        this.user = purchase.user();
        this.shopId = purchase.shopId();
        this.dateOfPurchase = purchase.dateOfPurchase();
        this.transactionId = purchase.transectionId();
        this.products = purchase.products().stream().map(ProductInfo::new).toList();
    }
}
