package BusinessLayer.System;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class ProxySupplyTest {
    public ProxySupply p = null;

    @Before
    public void setUp(){
        p = new ProxySupply();
    }

    @Test
    public void testCheckSupplySuccess() {
        Assert.assertTrue(p.checkSupply(new ProductInfo(new Product(1, "bud light", 10.0, 10))));
        Assert.assertTrue(p.checkSupply(new ProductInfo(new Product(2, "bud dark", 15.0, 5))));
    }

    @Test
    public void testCheckSupplyFailAmountZero() {
        Assert.assertFalse(p.checkSupply(new ProductInfo(new Product(3, "bud pro", 20.0, 0))));
    }

    @Test
    public void testCheckSupplyFailAmountNegative() {
        Assert.assertFalse(p.checkSupply(new ProductInfo(new Product(5, "bud max", 30.0, -4))));
    }
}