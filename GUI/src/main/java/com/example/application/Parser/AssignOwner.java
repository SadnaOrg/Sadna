package com.example.application.Parser;

import ServiceLayer.interfaces.UserService;

public class AssignOwner extends ParsedLine {
    private int shop;
    private String username;

    public AssignOwner(int shop, String username) {
        this.shop = shop;
        this.username = username;
    }

    @Override
    public UserService act(UserService u) throws RuntimeException {
        return ParsedLine.getIfSubUserService(u,s->ParsedLine.getUserService(s,s.assignShopOwner(OpenShop.getID(shop),username)));
    }
}
