package BusinessLayer.System;

import BusinessLayer.Shops.Purchase;

public interface Payment {
    boolean pay(double totalPrice, PaymentMethod method);
}
