package BusinessLayer.System;

import org.junit.*;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProxyPaymentUnitTest {
    Payment p;
    PaymentMethod method;
    public AtomicInteger month = new AtomicInteger(Calendar.getInstance().get(Calendar.MONTH));
    public AtomicInteger year = new AtomicInteger(Calendar.getInstance().get(Calendar.YEAR));

    @Before
    public void setUp(){
        p = new ProxyPayment();
        method = mock(PaymentMethod.class);
    }

    @Test
    public void testUnitPaySuccess() {
        when(method.isValidPaymentMethod(any(AtomicInteger.class), any(AtomicInteger.class))).thenReturn(true);
        assertTrue(p.pay(1000, method));
        assertTrue(p.pay(2000, method));
    }

    @Test
    public void testUnitPayFailureAmountZero(){
        when(method.isValidPaymentMethod(any(AtomicInteger.class), any(AtomicInteger.class))).thenReturn(true);
        assertFalse(p.pay(0, method));
    }

    @Test
    public void testUnitPayFailureAmountNegative(){
        when(method.isValidPaymentMethod(any(AtomicInteger.class), any(AtomicInteger.class))).thenReturn(true);
        assertFalse(p.pay(-100, method));
        assertFalse(p.pay(-1, method));
    }
}
