package com.example.application.Parser;

import ServiceLayer.Response;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.UserService;

import java.util.function.Supplier;

public class Logout extends ParsedLine {

    @Override
    public UserService act(UserService u) throws RuntimeException {
        return ParsedLine.getIfSubUserService(u,s -> ParsedLine.getUserService(s,s.logout()));
    }
}
