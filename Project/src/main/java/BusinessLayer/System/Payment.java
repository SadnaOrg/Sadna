package BusinessLayer.System;

public interface Payment {

    boolean pay(double totalPrice, PaymentMethod method, String ID, String cardHolder);
}
