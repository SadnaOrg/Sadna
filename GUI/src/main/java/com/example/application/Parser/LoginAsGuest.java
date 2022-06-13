package com.example.application.Parser;

import ServiceLayer.interfaces.UserService;
import org.springframework.remoting.RemoteTimeoutException;

public class LoginAsGuest implements ParsedLine{
    @Override
    public UserService act(UserService u) throws RuntimeException {
        var res = u.loginSystem();
        if(res.isOk())
            return u;
        else throw new RuntimeException(res.getMsg());
    }
}
