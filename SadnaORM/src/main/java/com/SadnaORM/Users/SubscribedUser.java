package com.SadnaORM.Users;

import javax.persistence.*;

@Entity
@Table(name = "SubscribedUser")
public class SubscribedUser {
    @Id
    private String username;
    private String password;
    private boolean is_login;
    private boolean isNotRemoved;
    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private PaymentMethod paymentMethod;
    // private Map<Integer,ShopAdministrator> administratorMap;
}
