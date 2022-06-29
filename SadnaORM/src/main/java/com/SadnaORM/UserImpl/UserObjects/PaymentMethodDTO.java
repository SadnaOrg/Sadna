package com.SadnaORM.UserImpl.UserObjects;

import com.SadnaORM.Users.User;


public class PaymentMethodDTO {
    private String creditCard;
    private int CVV;
    private int expirationYear;
    private int expirationDay;
    private int user;

    public PaymentMethodDTO(String creditCard, int CVV, int expirationYear, int expirationDay, int user) {
        this.creditCard = creditCard;
        this.CVV = CVV;
        this.expirationYear = expirationYear;
        this.expirationDay = expirationDay;
        this.user = user;
    }

    public PaymentMethodDTO() {
    }

    public String getCreditCard() {
        return this.creditCard;
    }

    public int getCVV() {
        return this.CVV;
    }

    public int getExpirationYear() {
        return this.expirationYear;
    }

    public int getExpirationDay() {
        return this.expirationDay;
    }
}
