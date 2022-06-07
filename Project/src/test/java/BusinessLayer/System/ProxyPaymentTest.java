package BusinessLayer.System;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProxyPaymentTest {

    Payment p = null;
    PaymentMethod method = new PaymentMethod("4580123456789012", 123, 11, 2026);

    @Before
    public void setUp() {
        p = new ProxyPayment();
    }

    @Test
    public void testPaySuccess() {
        Assert.assertTrue(p.pay(1000, method));
        Assert.assertTrue(p.pay(2000, method));
    }

    @Test
    public void testPayFailureAmountZero(){
        Assert.assertFalse(p.pay(0, method));
    }

    @Test
    public void testPayFailureAmountNegative(){
        Assert.assertFalse(p.pay(-100, method));
        Assert.assertFalse(p.pay(-1, method));
    }

    @Test
    public void testPayFailureCardInvalid(){
        PaymentMethod method = new PaymentMethod("1246", 123, 4, 2032);
        Assert.assertFalse(p.pay(100, method));
    }

    @Test
    public void testPayFailureCVVInvalid(){
        PaymentMethod method = new PaymentMethod("4580123456789012", 10, 4, 2032);
        Assert.assertFalse(p.pay(100, method));
    }

    @Test
    public void testPayFailureExpiryMonthInvalid(){
        PaymentMethod method = new PaymentMethod("4580123456789012", 123, 16, 1999);
        Assert.assertFalse(p.pay(100, method));
    }

    @Test
    public void testPayFailureExpiryYearInvalid(){
        PaymentMethod method = new PaymentMethod("4580123456789012", 123, 4, 1999);
        Assert.assertFalse(p.pay(100, method));
    }

    @Test
    public void testPayFailureExpiryYearValidButMonthInvalid(){
        PaymentMethod method = new PaymentMethod("4580123456789012", 123, 1, 2022);
        Assert.assertFalse(p.pay(100, method));
    }
}