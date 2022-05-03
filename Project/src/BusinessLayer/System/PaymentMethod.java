package BusinessLayer.System;

public class PaymentMethod {
    private String creditCardNumber;
    private int CVV;
    private String ExpiryDate;

    public PaymentMethod(String creditCardNumber, int CVV, String ExpiryDate) {
        this.creditCardNumber = creditCardNumber;
        this.CVV = CVV;
        this.ExpiryDate = ExpiryDate;
    }

    public int getCVV() {
        return CVV;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }
}
