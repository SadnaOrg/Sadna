package BusinessLayer.System;

import BusinessLayer.Shops.Purchase;

public abstract class Payment {
    int id;
    public abstract Purchase pay(int totalPrice, int transactionId);
}
