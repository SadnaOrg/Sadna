package System;

public class ProxyPayment extends Payment{
    private Payment payment = null;

    @Override
    public boolean pay(int totalPrice) {
        if(payment != null)
            return payment.pay(totalPrice);
        return false;
    }
}
