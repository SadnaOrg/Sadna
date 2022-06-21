package com.SadnaORM.Users;


import javax.persistence.*;
@Entity
@Table(name = "PaymentMethod")
public class PaymentMethod {
    @Id
    private String creditCard;
    private int CVV;
    private int expirationYear;
    private int expirationDay;
    @OneToOne
    private User user;

    public PaymentMethod(String creditCard, int CVV, int expirationYear, int expirationDay, User user) {
        this.creditCard = creditCard;
        this.CVV = CVV;
        this.expirationYear = expirationYear;
        this.expirationDay = expirationDay;
        this.user = user;
    }

    public PaymentMethod(){

    }

    public String getCreditCard() {
        return creditCard;
    }

    public int getCVV() {
        return CVV;
    }

    public int getExpirationYear() {
        return expirationYear;
    }

    public int getExpirationDay() {
        return expirationDay;
    }
}