package com.SadnaORM.Users;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Guest")
public class Guest extends User{
    public Guest(){

    }

    public Guest(String username, PaymentMethod method){
        super(username,method);
    }
}
