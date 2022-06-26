package com.example.application.Parser;

import ServiceLayer.interfaces.UserService;

public class AddProductToShop extends ParsedLine {
    private int shop;
    private String product_name;
    private String product_description;
    private String manufacture;
    private int productID;
    private int quantity;
    private double price;

    public AddProductToShop(int shop, String product_name, String product_description, String manufacture, int productID, int quantity, double price) {
        this.shop = shop;
        this.product_name = product_name;
        this.product_description = product_description;
        this.manufacture = manufacture;
        this.productID = productID;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public UserService act(UserService u) throws RuntimeException {
        try {
            return ParsedLine.getIfSubUserService(u, s -> ParsedLine.getUserService(s, s.addProductToShop(OpenShop.SHOP_LIST.get(shop).shopId(), product_name, product_description, manufacture, productID, quantity, price)));
        }
        catch (Exception e){
            throw new RuntimeException("shop dont exist");
        }
    }
}
