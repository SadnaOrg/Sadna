package BusinessLayer.System;

import BusinessLayer.Shops.Purchase;

public abstract class Payment {
    int id;
    public abstract boolean pay(double totalPrice, PaymentMethod method);
}
