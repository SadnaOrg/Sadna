package BusinessLayer.System;

import BusinessLayer.Shops.Purchase;

public class ProxyPayment extends Payment{
    private Payment payment = null;

    @Override
    public Purchase pay(int totalPrice) {
        if(payment != null)
            return payment.pay(totalPrice);
        return null;
    }
}
