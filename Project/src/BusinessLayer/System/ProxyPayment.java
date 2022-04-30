package BusinessLayer.System;

import BusinessLayer.Shops.Purchase;

public class ProxyPayment extends Payment{
    private Payment payment = null;

    @Override
    public boolean pay(double totalPrice) {
        if(payment != null)
            return payment.pay(totalPrice);
        return true;
    }
}
