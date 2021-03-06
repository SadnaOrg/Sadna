package BusinessLayer.System;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;


public class ProxyPayment implements Payment{
    private Payment payment = null;

    @Override
    public boolean pay(double totalPrice, PaymentMethod method, String ID, String cardHolder) {
        if(method == null)
            return false;
        AtomicInteger year = new AtomicInteger(Calendar.getInstance().get(Calendar.YEAR));
        AtomicInteger month = new AtomicInteger(Calendar.getInstance().get(Calendar.MONTH));
        if(!method.isValidPaymentMethod(month, year) || totalPrice <= 0)
            return false;
        if(payment != null)
            return payment.pay(totalPrice, method,ID,cardHolder);
        return true;
    }
}
