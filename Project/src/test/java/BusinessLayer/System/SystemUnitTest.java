package BusinessLayer.System;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SystemUnitTest {
    System system;
    PaymentMethod method;
    ExternalServicesSystem externSystem;
    PackageInfo success = mock(PackageInfo.class);
    PackageInfo fail = mock(PackageInfo.class);
    @Before
    public void setUp() {
        externSystem = mock(ExternalServicesSystem.class);
        method = mock(PaymentMethod.class);
        system = mock(System.class);
    }

    @Test
    public void testPay() {
        ConcurrentHashMap<Integer, Double> totalPrices = new ConcurrentHashMap<>();
        totalPrices.put(1, 10.0);
        totalPrices.put(2, -10.0);
        when(system.pay(totalPrices, method,"206000556","maor biton")).thenCallRealMethod();
        when(system.getExternSystem()).thenReturn(externSystem);
        when(externSystem.pay(anyDouble(), any(PaymentMethod.class),any(String.class),any(String.class))).thenReturn(true);
        ConcurrentHashMap<Integer, Boolean> res = system.pay(totalPrices, method,"206000556","maor biton");
        Assert.assertTrue(res.get(1));
        Assert.assertFalse(res.get(2));
    }

    @Test
    public void testCheckSupply() {
        ConcurrentHashMap<Integer, PackageInfo> packages = new ConcurrentHashMap<>();
        packages.put(1, success);
        packages.put(2, fail);
        when(system.checkSupply(packages)).thenCallRealMethod();
        when(system.getExternSystem()).thenReturn(externSystem);
        when(externSystem.checkSupply(packages.get(1))).thenReturn(true);
        when(externSystem.checkSupply(packages.get(2))).thenReturn(false);
        ConcurrentHashMap<Integer, Boolean> res = system.checkSupply(packages);
        Assert.assertTrue(res.get(1));
        Assert.assertFalse(res.get(2));
    }
}