package BusinessLayer.Shops;

import BusinessLayer.Users.Basket;

import java.time.ZonedDateTime;
import java.util.Date;

public class Purchase {
    int transectionid;
    Basket info;
    Date dateOfPurchase;

    public Purchase(int transactionid, Basket info)
    {
        transectionid++;
        this.info =info;
        this.dateOfPurchase= new Date();
    }

}
