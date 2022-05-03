package BusinessLayer.System;

import BusinessLayer.Shops.Purchase;

public class ProxyPayment extends Payment{
    private Payment payment = null;

    @Override
    public boolean pay(double totalPrice, PaymentMethod method) {
        if(payment != null && method != null)
            return payment.pay(totalPrice, method);
        return true;
    }
}
