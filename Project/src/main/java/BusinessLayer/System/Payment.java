package main.java.BusinessLayer.System;

import main.java.BusinessLayer.Shops.Purchase;

public interface Payment {
    boolean pay(double totalPrice, PaymentMethod method);
}
