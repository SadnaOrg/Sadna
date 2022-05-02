package BusinessLayer.System;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProxyPaymentTest {

    Payment p = null;
    PaymentMethod method = new PaymentMethod("4580123456789012", 123, "1/29");

    @Before
    public void setUp() {
        p = new ProxyPayment();
    }

    @Test
    public void pay() {
        Assert.assertTrue(p.pay(1000, method));
        Assert.assertTrue(p.pay(2000, method));
    }
}