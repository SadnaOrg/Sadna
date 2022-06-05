package com.SadnaORM.Users;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User {
    @Id
    protected String username;
    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    protected PaymentMethod paymentMethod;

    public User(String username, PaymentMethod paymentMethod){
        this.username = username;
        this.paymentMethod = paymentMethod;
    }

    public User(){

    }

    public String getUsername() {
        return username;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
