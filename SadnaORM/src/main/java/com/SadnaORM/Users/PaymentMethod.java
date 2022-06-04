package com.SadnaORM.Users;

import javax.persistence.*;

@Entity
@Table(name = "PaymentMethod")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private String creditCard;
    private int CVV;
    private int expirationYear;
    private int expirationDay;
    @OneToOne
    private SubscribedUser user;

}
