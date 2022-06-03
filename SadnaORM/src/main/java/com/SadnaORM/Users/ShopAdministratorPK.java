package com.SadnaORM.Users;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ShopAdministratorPK implements Serializable {
    private int shopID;
    private String username;

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
