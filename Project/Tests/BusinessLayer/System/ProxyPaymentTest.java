package BusinessLayer.System;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProxyPaymentTest {

    Payment p = null;

    @Before
    public void setUp() {
        p = new ProxyPayment();
    }

    @Test
    public void pay() {
        Assert.assertNull(p.pay(1000, 4));
        Assert.assertNull(p.pay(2000, 100));
    }
}