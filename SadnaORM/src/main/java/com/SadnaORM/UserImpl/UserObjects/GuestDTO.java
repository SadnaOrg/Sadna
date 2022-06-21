package com.SadnaORM.UserImpl.UserObjects;

import com.SadnaORM.Users.PaymentMethod;

public class GuestDTO extends UserDTO {
    public GuestDTO(){

    }

    public GuestDTO(String username, PaymentMethodDTO method){
        super(username,method);
    }

    public GuestDTO(String username){
        this.username = username;
        this.paymentMethod = null;
    }
}
