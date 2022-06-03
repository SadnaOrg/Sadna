package com.SadnaORM.Users;

import javax.persistence.*;
import java.util.List;

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
    @OneToMany(mappedBy = "user")
    private List<ShopAdministrator> administrators;
}
