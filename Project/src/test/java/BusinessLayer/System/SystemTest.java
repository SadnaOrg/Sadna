package BusinessLayer.System;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SystemTest {
    System system;
    ConcurrentHashMap<Integer, Double> prices;
    PaymentMethod method = new PaymentMethod("4580123456789012", 123, 11, 2026);
    @Before
    public void setUp() {
        system = System.getInstance();
        system.initialize();
        system.addSupply(new ProxySupply());
        system.addPayment(new ProxyPayment());
        productInfos = new ConcurrentLinkedDeque<>();
        prices = new ConcurrentHashMap<>();
    }

    @Test
    public void testPaySuccess() {
        prices.put(1, 1000.0);
        prices.put(2, 2000.0);
        ConcurrentHashMap<Integer, Boolean> res = system.pay(prices, method);
        Assert.assertTrue(res.get(1));
        Assert.assertTrue(res.get(2));
    }

    @Test
    public void testPayFailureAmountZero(){
        prices.put(1, 0.0);
        ConcurrentHashMap<Integer, Boolean> res = system.pay(prices, method);
        Assert.assertFalse(res.get(1));
    }

    @Test
    public void testPayFailureAmountNegative(){
        prices.put(1, -100.0);
        ConcurrentHashMap<Integer, Boolean> res = system.pay(prices, method);
        Assert.assertFalse(res.get(1));
    }

    @Test
    public void testPayFailureCardInvalid(){
        PaymentMethod method = new PaymentMethod("1246", 123, 4, 2032);
        prices.put(1, 100.0);
        ConcurrentHashMap<Integer, Boolean> res = system.pay(prices, method);
        Assert.assertFalse(res.get(1));
    }

    @Test
    public void testPayFailureCVVInvalid(){
        PaymentMethod method = new PaymentMethod("4580123456789012", 10, 4, 2032);
        prices.put(1, 100.0);
        ConcurrentHashMap<Integer, Boolean> res = system.pay(prices, method);
        Assert.assertFalse(res.get(1));
    }

    @Test
    public void testPayFailureExpiryMonthInvalid(){
        PaymentMethod method = new PaymentMethod("4580123456789012", 123, 16, 1999);
        prices.put(1, 100.0);
        ConcurrentHashMap<Integer, Boolean> res = system.pay(prices, method);
        Assert.assertFalse(res.get(1));
    }

    @Test
    public void testPayFailureExpiryYearInvalid(){
        PaymentMethod method = new PaymentMethod("4580123456789012", 123, 4, 1999);
        prices.put(1, 100.0);
        ConcurrentHashMap<Integer, Boolean> res = system.pay(prices, method);
        Assert.assertFalse(res.get(1));
    }

    @Test
    public void testPayFailureExpiryYearValidButMonthInvalid(){
        PaymentMethod method = new PaymentMethod("4580123456789012", 123, 1, 2022);
        prices.put(1, 100.0);
        ConcurrentHashMap<Integer, Boolean> res = system.pay(prices, method);
        Assert.assertFalse(res.get(1));
    }

    ProductInfo successProd = new ProductInfo(new Product(1, "name", 45.0, 150));
    ProductInfo anotherSuccessProd = new ProductInfo(new Product(1, "name", 16.0, 30));
    ProductInfo negativeProd = new ProductInfo(new Product(1, "name", 160.0, -10));
    ProductInfo zeroProd = new ProductInfo(new Product(1, "name", 160.0, 0));
    ConcurrentLinkedDeque<ProductInfo> productInfos = new ConcurrentLinkedDeque<>();

    @Test
    public void testCheckSupplySuccess() {
        productInfos.add(successProd);
        productInfos.add(anotherSuccessProd);
        PackageInfo successPack = new PackageInfo(new AtomicInteger(1), "home", productInfos);
        ConcurrentHashMap<Integer, PackageInfo> packages = new ConcurrentHashMap<>();
        packages.put(1, successPack);
        ConcurrentHashMap<Integer, Boolean> res = system.checkSupply(packages);
        Assert.assertTrue(res.get(1));
    }

    @Test
    public void testCheckSupplyFailZeroQuantity() {
        productInfos.add(successProd);
        productInfos.add(zeroProd);
        PackageInfo zeroPack = new PackageInfo(new AtomicInteger(1), "home", productInfos);
        ConcurrentHashMap<Integer, PackageInfo> packages = new ConcurrentHashMap<>();
        packages.put(1, zeroPack);
        ConcurrentHashMap<Integer, Boolean> res = system.checkSupply(packages);
        Assert.assertFalse(res.get(1));
    }

    @Test
    public void testCheckSupplyFailNegativeQuality() {
        productInfos.add(successProd);
        productInfos.add(negativeProd);
        PackageInfo zeroPack = new PackageInfo(new AtomicInteger(1), "home", productInfos);
        ConcurrentHashMap<Integer, PackageInfo> packages = new ConcurrentHashMap<>();
        packages.put(1, zeroPack);
        ConcurrentHashMap<Integer, Boolean> res = system.checkSupply(packages);
        Assert.assertFalse(res.get(1));
    }

    @Test
    public void testMultithreadedAddPayment() throws InterruptedException {
        Thread t1 = new Thread(()->system.addPayment(new ProxyPayment()));
        Thread t2 = new Thread(()->system.addPayment(new ProxyPayment()));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        Assert.assertEquals(4, system.getPaymentSize());
    }

    @Test
    public void testMultithreadedAddSupply() throws InterruptedException {
        Thread t1 = new Thread(()->system.addSupply(new ProxySupply()));
        Thread t2 = new Thread(()->system.addSupply(new ProxySupply()));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        Assert.assertEquals(3, system.getSupplySize());
    }
}
