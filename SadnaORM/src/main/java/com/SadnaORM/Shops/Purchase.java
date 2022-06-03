package com.SadnaORM.Shops;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "purchase")
public class Purchase {
    @Id
    int transactionid;
    Date dateOfPurchase;
    int shopid;
    String user;
}
