package BusinessLayer.System;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ProxySupplyTest {
    public ProxySupply p = null;
    ConcurrentHashMap<AtomicInteger, ProductInfo> products = null;

    @Before
    public void setUp(){
        p = new ProxySupply();
        products = new ConcurrentHashMap<>();
    }

    @Test
    public void testCheckSupplySuccess() {
        products.put(new AtomicInteger(1), new ProductInfo(new Product(1, "bud light", 10.0, 10)));
        products.put(new AtomicInteger(1), new ProductInfo(new Product(2, "bud dark", 15.0, 5)));
        Assert.assertTrue(p.checkSupply(new PackageInfo(new AtomicInteger(1), "my home", products)));
    }

    @Test
    public void testCheckSupplyFailAmountZero() {
        products.put(new AtomicInteger(1), new ProductInfo(new Product(3, "bud pro", 20.0, 0)));
        products.put(new AtomicInteger(1), new ProductInfo(new Product(4, "bud plus", 25.0, 5)));
        Assert.assertFalse(p.checkSupply(new PackageInfo(new AtomicInteger(1), "my home", products)));
    }

    @Test
    public void testCheckSupplyFailAmountNegative() {
        products.put(new AtomicInteger(1), new ProductInfo(new Product(5, "bud max", 30.0, -4)));
        products.put(new AtomicInteger(1), new ProductInfo(new Product(6, "bud ultra", 25.0, 5)));
        Assert.assertFalse(p.checkSupply(new PackageInfo(new AtomicInteger(1), "my home", products)));
    }
}