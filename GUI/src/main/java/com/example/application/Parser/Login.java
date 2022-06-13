package com.example.application.Parser;

import ServiceLayer.interfaces.UserService;

public class Login implements ParsedLine{
    private String username;
    private String password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public UserService act(UserService u) throws RuntimeException {
        var res = u.login(username,password);
        return ParsedLine.getUserService(u,res);
    }
}
