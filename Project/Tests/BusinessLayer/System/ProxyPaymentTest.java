package BusinessLayer.System;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class ProxyPaymentTest {

    Payment p = null;
    PaymentMethod method = new PaymentMethod("4580123456789012", new AtomicInteger(123), new AtomicInteger(11), new AtomicInteger(2026));

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
        PaymentMethod method = new PaymentMethod("1246", new AtomicInteger(123), new AtomicInteger(4), new AtomicInteger(2032));
        Assert.assertFalse(p.pay(100, method));
    }

    @Test
    public void testPayFailureCVVInvalid(){
        PaymentMethod method = new PaymentMethod("4580123456789012", new AtomicInteger(10), new AtomicInteger(4), new AtomicInteger(2032));
        Assert.assertFalse(p.pay(100, method));
    }

    @Test
    public void testPayFailureExpiryMonthInvalid(){
        PaymentMethod method = new PaymentMethod("4580123456789012", new AtomicInteger(123), new AtomicInteger(16), new AtomicInteger(1999));
        Assert.assertFalse(p.pay(100, method));
    }

    @Test
    public void testPayFailureExpiryYearInvalid(){
        PaymentMethod method = new PaymentMethod("4580123456789012", new AtomicInteger(123), new AtomicInteger(4), new AtomicInteger(1999));
        Assert.assertFalse(p.pay(100, method));
    }

    @Test
    public void testPayFailureExpiryYearValidButMonthInvalid(){
        PaymentMethod method = new PaymentMethod("4580123456789012", new AtomicInteger(123), new AtomicInteger(1), new AtomicInteger(2022));
        Assert.assertFalse(p.pay(100, method));
    }
}