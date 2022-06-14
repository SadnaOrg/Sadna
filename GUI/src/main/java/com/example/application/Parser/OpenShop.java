package com.example.application.Parser;

import ServiceLayer.Objects.Shop;
import ServiceLayer.interfaces.UserService;

import java.util.ArrayList;
import java.util.List;

public class OpenShop implements ParsedLine{
    public static final List<Shop> SHOP_LIST= new ArrayList<>();
    private String name;
    private String description;

    public OpenShop(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public UserService act(UserService u) throws RuntimeException {
        return ParsedLine.getIfSubUserService(u,s->ParsedLine.getUserServiceRes(s,s.openShop(name,description).safe(shop ->{SHOP_LIST.add(shop);return shop;})));
    }
    public static int getID(int shop){
        if(SHOP_LIST.size()>shop)
            return SHOP_LIST.get(shop).shopId();
        else throw new RuntimeException("shop "+shop+" does not exist");
    }
}
