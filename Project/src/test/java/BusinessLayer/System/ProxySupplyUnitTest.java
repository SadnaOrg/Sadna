package BusinessLayer.System;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProxySupplyUnitTest {
    public ProxySupply p = null;
    ConcurrentHashMap<AtomicInteger, ProductInfo> products = null;

    @Before
    public void setUp(){
        p = new ProxySupply();
        products = new ConcurrentHashMap<>();
    }

    @Test
    public void testCheckSupplySuccess() {
        ProductInfo successProd = new ProductInfo(new Product(1, "bud light", 15, 10));
        ProductInfo anotherSuccessProd = new ProductInfo(new Product(2, "bud dark", 20, 30));
        Assert.assertTrue(p.checkSupply(successProd));
        Assert.assertTrue(p.checkSupply(anotherSuccessProd));
    }

    @Test
    public void testCheckSupplyFailAmountZero() {
        ProductInfo zeroQuantityProd = new ProductInfo(new Product(2, "bud pro", 25, 0));
        Assert.assertFalse(p.checkSupply(zeroQuantityProd));
    }

    @Test
    public void testCheckSupplyFailAmountNegative() {
        ProductInfo negativeQuantityProd = new ProductInfo(new Product(2, "bud max", 17, -5));
        Assert.assertFalse(p.checkSupply(negativeQuantityProd));
    }
}






