package com.example.application.Parser;

import ServiceLayer.interfaces.UserService;

public class AddToCart extends ParsedLine {

    private int shop;
    private int product;
    private int quantity;

    public AddToCart(int shop, int product, int quantity) {
        this.shop = shop;
        this.product = product;
        this.quantity = quantity;
    }

    @Override
    public UserService act(UserService u) throws RuntimeException {
        if(OpenShop.SHOP_LIST.size()>shop)
            shop = OpenShop.SHOP_LIST.get(shop).shopId();
        var res = u.saveProducts(shop,product,quantity);
        return ParsedLine.getUserService(u, res);
    }
}
