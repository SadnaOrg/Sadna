package com.example.application.views.main;

public class PaymentMethod {
    private String creditCardNumber;
    private int cvv;
    private int month;
    private int year;

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCvv() {
        return String.valueOf(cvv);
    }

    public void setCvv(String cvv) {
        this.cvv = Integer.parseInt(cvv);
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
