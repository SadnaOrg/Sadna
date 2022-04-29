package BusinessLayer.System;

import BusinessLayer.Shops.Purchase;

public class ProxyPayment extends Payment{
    private Payment payment = null;

    @Override
    public Purchase pay(int totalPrice, int transactionId) {
        if(payment != null)
            return payment.pay(totalPrice, transactionId);
        return null;
    }
}
