package com.example.application.Parser;

import ServiceLayer.interfaces.UserService;
import org.springframework.remoting.RemoteTimeoutException;

public class LoginAsGuest extends ParsedLine {

    @Override
    public UserService act(UserService u) throws RuntimeException {
        var res = u.loginSystem();
        return ParsedLine.getUserService(u, res);
    }
}
