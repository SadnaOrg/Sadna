package com.SadnaORM.UserImpl.UserObjects;

import com.SadnaORM.Users.PaymentMethod;

public abstract class UserDTO {
    protected String username;
    protected PaymentMethodDTO paymentMethod;

    public UserDTO(String username, PaymentMethodDTO paymentMethod){
        this.username = username;
        this.paymentMethod = paymentMethod;
    }

    public UserDTO(){

    }

    public String getUsername() {
        return username;
    }

    public PaymentMethodDTO getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodDTO paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
