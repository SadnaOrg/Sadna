//package BusinessLayer.System;
//
//import main.java.BusinessLayer.System.Payment;
//import main.java.BusinessLayer.System.PaymentMethod;
//import main.java.BusinessLayer.System.ProxyPayment;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class ProxyPaymentTest {
//
//    Payment p = null;
//    PaymentMethod method = new PaymentMethod("4580123456789012", 123, "1/29");
//
//    @BeforeAll
//    public void setUp() {
//        p = new ProxyPayment();
//    }
//
//    @Test
//    public void pay() {
//        assertTrue(p.pay(1000, method));
//        assertTrue(p.pay(2000, method));
//    }
//}