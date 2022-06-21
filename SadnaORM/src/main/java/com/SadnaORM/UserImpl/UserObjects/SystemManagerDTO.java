package com.SadnaORM.UserImpl.UserObjects;

import com.SadnaORM.Users.PaymentMethod;

import java.util.List;

public class SystemManagerDTO extends SubscribedUserDTO {
    public SystemManagerDTO() {
    }

    public SystemManagerDTO(String username, String password, boolean is_login, boolean isNotRemoved, PaymentMethodDTO paymentMethod){
        super(username, password, is_login, isNotRemoved, paymentMethod);

    }

    public SystemManagerDTO(String username, String password, boolean is_login, boolean isNotRemoved, PaymentMethodDTO paymentMethod, List<ShopAdministratorDTO> administrators){
        super(username,password,is_login,isNotRemoved,paymentMethod,administrators);
    }
}
