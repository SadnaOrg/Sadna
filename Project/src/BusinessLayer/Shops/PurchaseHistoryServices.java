package BusinessLayer.Shops;

import java.util.ArrayList;
import java.util.Collection;

public class PurchaseHistoryServices {


    private Collection<PurchaseHistory> DataOnPurchases;

    public PurchaseHistoryServices()
    {
        this.DataOnPurchases = new ArrayList<>();
    }

    public Collection<PurchaseHistory> getDataOnPurchases() {
        return DataOnPurchases;
    }

    public PurchaseHistory createPurchaseHistory(int shopid, String user)
    {
        return new PurchaseHistory(shopid , user);
    }
}
