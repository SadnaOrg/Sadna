package BusinessLayer.System;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExternalServicesSystemUnitTest {
    public ExternalServicesSystem system = null;
    Supply supply;
    Supply otherSupply;
    Payment payment;
    Payment failedPayment;
    PaymentMethod method;
    ProductInfo successProd;
    ProductInfo anotherSuccessProd;

    @Before
    public void setUp(){
        system = new ExternalServicesSystem();
        supply = mock(Supply.class);
        otherSupply = mock(Supply.class);
        payment = mock(Payment.class);
        failedPayment = mock(Payment.class);
        method = mock(PaymentMethod.class);
        successProd = new ProductInfo(new Product(1, "bud light", 15, 10));
        anotherSuccessProd = new ProductInfo(new Product(2, "bud dark", 20, 30));
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
        Collection<ProductInfo> products = setUpSupply(new ArrayList<>());
        when(supply.checkSupply(successProd)).thenReturn(true);
        when(supply.checkSupply(anotherSuccessProd)).thenReturn(true);
        Assert.assertTrue(system.checkSupply(new PackageInfo(new AtomicInteger(1), "home", products)));
    }

    @Test
    public void testCheckSupplyMultipleSuppliersSuccess() {
        Collection<ProductInfo> products = setUpSupply(new ArrayList<>());
        when(supply.checkSupply(successProd)).thenReturn(true);
        when(otherSupply.checkSupply(anotherSuccessProd)).thenReturn(true);
        Assert.assertTrue(system.checkSupply(new PackageInfo(new AtomicInteger(1), "home", products)));
    }

    @Test
    public void testCheckSupplyMultipleSuppliersFail() {
        Collection<ProductInfo> products = setUpSupply(new ArrayList<>());
        when(supply.checkSupply(successProd)).thenReturn(true);
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
        Assert.assertEquals(2, system.getPaymentSize());
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
        when(payment.pay(Mockito.doubleThat((arg) -> arg > 0), any(PaymentMethod.class))).thenReturn(true);
    }

    public Collection<ProductInfo> setUpSupply(Collection<ProductInfo> products){
        system.addSupply(supply);
        system.addSupply(otherSupply);
        products.add(successProd);
        products.add(anotherSuccessProd);
        return products;
    }
}