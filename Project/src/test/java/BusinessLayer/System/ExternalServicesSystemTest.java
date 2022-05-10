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
        method = new PaymentMethod("4580123456789012", new AtomicInteger(266), new AtomicInteger(4), new AtomicInteger(2032));
        successProd = new ProductInfo(new Product(1, "bud light", 15, 10));
        anotherSuccessProd = new ProductInfo(new Product(2, "bud dark", 20, 30));
        failProd = new ProductInfo(new Product(3, "bud new", 25, 0));
        setUpPay();
        setUpSupply();
    }

    @Test
    public void testCheckPaymentSuccess() {
        Assert.assertTrue(system.pay(100, method));
    }

    @Test
    public void testPayFailAmountZero() {
        Assert.assertFalse(system.pay(0, method));
    }

    @Test
    public void testPayFailAmountNegative() {
        Assert.assertFalse(system.pay(-100, method));
    }

    @Test
    public void testCheckSupplySuccess(){
        Collection<ProductInfo> products = new ArrayList<>();
        products.add(successProd);
        products.add(anotherSuccessProd);
        Assert.assertTrue(system.checkSupply(new PackageInfo(new AtomicInteger(1), "home", products)));
    }

    @Test
    public void testCheckSupplyMultipleSuppliersSuccess() {
        Collection<ProductInfo> products = new ArrayList<>();
        products.add(successProd);
        products.add(anotherSuccessProd);
        Assert.assertTrue(system.checkSupply(new PackageInfo(new AtomicInteger(1), "home", products)));
    }

    @Test
    public void testCheckSupplyMultipleSuppliersFail() {
        Collection<ProductInfo> products = new ArrayList<>();
        products.add(successProd);
        products.add(anotherSuccessProd);
        products.add(failProd);
        Assert.assertFalse(system.checkSupply(new PackageInfo(new AtomicInteger(1), "home", products)));
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
