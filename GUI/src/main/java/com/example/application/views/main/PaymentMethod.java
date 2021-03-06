package com.example.application.views.main;

public class PaymentMethod {
    private String creditCardNumber;
    private int cvv;
    private int month;
    private int year;
    String cardHolder;
    String ID;

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCvv() {
        return String.valueOf(cvv);
    }

    public int getIntCvv(){
        return cvv;
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

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getID() {
        return ID;
    }
}
