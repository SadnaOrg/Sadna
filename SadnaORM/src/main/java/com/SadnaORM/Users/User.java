package com.SadnaORM.Users;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
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
}
