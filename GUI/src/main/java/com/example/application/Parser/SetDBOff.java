package com.example.application.Parser;

import ServiceLayer.interfaces.UserService;

public class SetDBOff implements ParsedLine{
    @Override
    public UserService act(UserService u) throws RuntimeException {
        return ParsedLine.getUserService(u,u.setDBOff());
    }
}
