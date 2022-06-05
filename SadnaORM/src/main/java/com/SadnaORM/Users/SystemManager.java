package com.SadnaORM.Users;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class SystemManager extends SubscribedUser{
    public SystemManager() {
    }

    public SystemManager(String username, String password, boolean is_login, boolean isNotRemoved, PaymentMethod paymentMethod){
        super(username, password, is_login, isNotRemoved, paymentMethod);

    }

    public SystemManager(String username, String password, boolean is_login, boolean isNotRemoved, PaymentMethod paymentMethod, List<ShopAdministrator> administrators){
        super(username,password,is_login,isNotRemoved,paymentMethod,administrators);
    }
}