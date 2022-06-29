package com.SadnaORM.UserImpl.UserObjects;

import java.util.HashMap;
import java.util.Map;

public class BasketDTO {

    private int shopId;
    private String userId;
    private Map<Integer,Integer> products;

    public BasketDTO() {
    }

    public BasketDTO(String username, int shopId, Map<Integer, Integer> products) {
        this.shopId = shopId;
        this.userId = username;
        this.products = products;
    }

    public BasketDTO(String username, int shopId){
        this.shopId = shopId;
        this.userId = username;
        this.products = new HashMap<>();
    }

    public void setProducts(Map<Integer, Integer> products) {
        this.products = products;
    }

    public int getShop() {
        return shopId;
    }

    public String getUser() {
        return userId;
    }

    public Map<Integer,Integer> getProducts() {
        return products;
    }

}
