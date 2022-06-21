package com.example.application.Parser;

import ServiceLayer.interfaces.UserService;

public class RemoveProduct implements ParsedLine{
    private int shop;
    private int product;

    public RemoveProduct(int shop, int product) {
        this.shop = shop;
        this.product = product;
    }

    @Override
    public UserService act(UserService u) throws RuntimeException {
        return ParsedLine.getUserService(u,u.removeProduct(OpenShop.getID(shop),product));
    }
}
