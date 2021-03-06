package BusinessLayer.System;

import java.util.concurrent.atomic.AtomicInteger;

public class PaymentMethod {
    private String creditCardNumber;
    private AtomicInteger CVV;
    private AtomicInteger expiryMonth;
    private AtomicInteger expiryYear;

    public PaymentMethod(String creditCardNumber, AtomicInteger CVV, AtomicInteger expiryMonth, AtomicInteger expiryYear) {
        this.creditCardNumber = creditCardNumber;
        this.CVV = CVV;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        if(!isValidPaymentMethod(expiryMonth,expiryYear))
            throw new IllegalArgumentException("un valid expierd date");
        if(!isValidCreditCardNumber())
            throw new IllegalArgumentException("un valid card number");

        if(!isValidCVV())
            throw new IllegalArgumentException("un valid cvv");
    }

    public PaymentMethod(String creditCardNumber, int cvv, int expiryMonth, int expiryYear) {
        this(creditCardNumber,new AtomicInteger(cvv),new AtomicInteger(expiryMonth),new AtomicInteger(expiryYear));



    }

    public AtomicInteger getCVV() {
        return CVV;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public AtomicInteger getExpiryMonth() {
        return expiryMonth;
    }

    public AtomicInteger getExpiryYear() {
        return expiryYear;
    }

    private boolean isValidCreditCardNumber(){
        if(getCreditCardNumber().length() != 16)
            return false;
        return getCreditCardNumber().chars().allMatch( Character::isDigit );
    }

    public boolean isValidCVV(){
        return (getCVV().get() >= 0 && getCVV().get() <= 999);
    }

    public boolean isValidExpiryDate(AtomicInteger month, AtomicInteger year){
        if(getExpiryMonth().get() > 12 || getExpiryMonth().get() < 1)
            return false;
        return getExpiryYear().get() >= year.get() && (getExpiryYear().get() != year.get() || getExpiryMonth().get() >= month.get());
    }

    public boolean isValidPaymentMethod(AtomicInteger month, AtomicInteger year){
        return isValidCVV() && isValidCreditCardNumber() && isValidExpiryDate(month, year);
    }
}
