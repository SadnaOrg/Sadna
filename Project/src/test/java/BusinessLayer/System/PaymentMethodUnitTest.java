package BusinessLayer.System;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.fail;

public class PaymentMethodUnitTest {
    public AtomicInteger month = new AtomicInteger(Calendar.getInstance().get(Calendar.MONTH));
    public AtomicInteger year = new AtomicInteger(Calendar.getInstance().get(Calendar.YEAR));

    @Test (expected = Exception.class)
    public void testPayFailureCardInvalid(){
        PaymentMethod method = new PaymentMethod("1246", 123, 4, 2032);
        fail();
    }

    @Test (expected = Exception.class)
    public void testPayFailureCVVInvalid(){
        PaymentMethod method = new PaymentMethod("4580123456789012", 5500, 4, 2032);
        fail();
    }

    @Test (expected = Exception.class)
    public void testPayFailureExpiryMonthInvalid(){
        PaymentMethod method = new PaymentMethod("4580123456789012", 123, 16, 2032);
        fail();
    }

    @Test
    public void testPayFailureExpiryYearInvalid(){
        PaymentMethod method = new PaymentMethod("4580123456789012", 123, 4, 1999);
        Assert.assertFalse(method.isValidPaymentMethod(month, year));
    }

    @Test
    public void testPayFailureExpiryYearValidButMonthInvalid() {
        PaymentMethod method = new PaymentMethod("4580123456789012", 123, 1, 2022);
        Assert.assertFalse(method.isValidPaymentMethod(month, year));
    }

    @Test
    public void testValidPaymentMethod(){
        PaymentMethod method = new PaymentMethod("4580123456789012", 123, 10, 2032);
        Assert.assertTrue(method.isValidPaymentMethod(month, year));
    }
}
