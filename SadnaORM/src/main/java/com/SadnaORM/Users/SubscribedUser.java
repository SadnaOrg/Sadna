package com.SadnaORM.Users;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "SubscribedUser")
public class SubscribedUser {
    public SubscribedUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.is_login = false;
        this.isNotRemoved = false;
    }

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

    public SubscribedUser() {

    }
}
