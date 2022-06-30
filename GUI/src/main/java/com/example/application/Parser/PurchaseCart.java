package com.example.application.Parser;

import ServiceLayer.interfaces.UserService;

public class PurchaseCart extends ParsedLine {
    private String credit_number;
    private int cvv;
    private int expierd_month;
    private int expierd_year;
    private String ID;
    private String cardHolder;

    public PurchaseCart(String credit_number, int cvv, int expierd_month, int expierd_year, String ID, String cardHolder) {
        this.credit_number = credit_number;
        this.cvv = cvv;
        this.expierd_month = expierd_month;
        this.expierd_year = expierd_year;
        this.ID = ID;
        this.cardHolder = cardHolder;
    }

    @Override
    public UserService act(UserService u) throws RuntimeException {
        return ParsedLine.getUserService(u,u.purchaseCartFromShop(credit_number,cvv,expierd_month,expierd_year,ID,cardHolder));
    }
}
