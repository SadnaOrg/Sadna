package com.example.application.Parser;

import ServiceLayer.interfaces.UserService;

public class AssignManager extends ParsedLine {
    private int shop;
    private String username;

    public AssignManager(int shop, String username) {
        this.shop = shop;
        this.username = username;
    }

    @Override
    public UserService act(UserService u) throws RuntimeException {
        return ParsedLine.getIfSubUserService(u,s->ParsedLine.getUserService(s,s.assignShopManager(OpenShop.getID(shop),username)));
    }
}
