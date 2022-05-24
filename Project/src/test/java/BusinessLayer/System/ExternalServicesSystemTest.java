package BusinessLayer.System;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class ExternalServicesSystemTest {
    public ExternalServicesSystem system = null;
    Supply supply;
    Supply otherSupply;
    Payment payment;
    Payment failedPayment;
    PaymentMethod method;
    ProductInfo successProd;
    ProductInfo anotherSuccessProd;
    ProductInfo failProd;

    @Before
    public void setUp(){
        system = new ExternalServicesSystem();
        supply = new ProxySupply();
        otherSupply = new ProxySupply();
        payment = new ProxyPayment();
        failedPayment = new ProxyPayment();
        method = new PaymentMethod("4580123456789012", 266, 4, 2032);
        successProd = new ProductInfo(new Product(1, "bud light", 15, 10));
        anotherSuccessProd = new ProductInfo(new Product(2, "bud dark", 20, 30));
        failProd = new ProductInfo(new Product(3, "bud new", 25, 0));
    }

    @Test
    public void testCheckPaymentSuccess() {
        setUpPay();
        Assert.assertTrue(system.pay(100, method));
    }

    @Test
    public void testPayFailAmountZero() {
        setUpPay();
        Assert.assertFalse(system.pay(0, method));
    }

    @Test
    public void testPayFailAmountNegative() {
        setUpPay();
        Assert.assertFalse(system.pay(-100, method));
    }

    @Test
    public void testCheckSupplySuccess(){
        setUpSupply();
        Collection<ProductInfo> products = new ArrayList<>();
        products.add(successProd);
        products.add(anotherSuccessProd);
        Assert.assertTrue(system.checkSupply(new PackageInfo(new AtomicInteger(1), "home", products)));
    }

    @Test
    public void testCheckSupplyMultipleSuppliersSuccess() {
        setUpSupply();
        Collection<ProductInfo> products = new ArrayList<>();
        products.add(successProd);
        products.add(anotherSuccessProd);
        Assert.assertTrue(system.checkSupply(new PackageInfo(new AtomicInteger(1), "home", products)));
    }

    @Test
    public void testCheckSupplyMultipleSuppliersFail() {
        setUpSupply();
        Collection<ProductInfo> products = new ArrayList<>();
        products.add(successProd);
        products.add(anotherSuccessProd);
        products.add(failProd);
        Assert.assertFalse(system.checkSupply(new PackageInfo(new AtomicInteger(1), "home", products)));
    }

    @Test
    public void testMultithreadedAddPayment() throws InterruptedException {
        Thread t1 = new Thread(()->system.addPayment(failedPayment));
        Thread t2 = new Thread(()->system.addPayment(payment));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        Assert.assertEquals(3, system.getPaymentSize());
    }

    @Test
    public void testMultithreadedAddSupply() throws InterruptedException {
        Thread t1 = new Thread(()->system.addSupply(supply));
        Thread t2 = new Thread(()->system.addSupply(otherSupply));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        Assert.assertEquals(2, system.getSupplySize());
    }

    public void setUpPay(){
        system.addPayment(failedPayment);
        system.addPayment(payment);
    }

    public void setUpSupply(){
        system.addSupply(supply);
        system.addSupply(otherSupply);
    }
}
