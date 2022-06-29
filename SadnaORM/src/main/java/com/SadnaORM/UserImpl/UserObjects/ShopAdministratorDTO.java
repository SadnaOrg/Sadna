package com.SadnaORM.UserImpl.UserObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class ShopAdministratorDTO implements Serializable{

    private List<BaseActionType> action;
    private int shopId;
    private String userId;
    private int appointer;

    public ShopAdministratorDTO(List<BaseActionType> action, String username, int shopId, int appointer) {
        this.action = action;
        this.userId = username;
        this.shopId = shopId;
        this.appointer = appointer;
    }

    public ShopAdministratorDTO(List<BaseActionType> action, String username, int shopId) {
        this.action = action;
        this.userId = username;
        this.shopId = shopId;
    }

    public ShopAdministratorDTO(){

    }

    public List<BaseActionType> getAction() {
        return action;
    }

    public void setAction(List<BaseActionType> action) {
        this.action = action;
    }

    public int getShopId() {
        return shopId;
    }

    public String getUserId() {
        return userId;
    }

    public int getAppointer() {
        return appointer;
    }

    public void setAppoints(int appointer) {
        this.appointer = appointer;
    }

    public enum BaseActionType {
        STOCK_MANAGEMENT(1),
        SET_PURCHASE_POLICY(2),
        ASSIGN_SHOP_OWNER(4),
        ASSIGN_SHOP_MANAGER(6),
        CHANGE_MANAGER_PERMISSION(7),
        CLOSE_SHOP(9),
        REOPEN_SHOP(10),
        ROLE_INFO(11),
        HISTORY_INFO(13),
        REMOVE_ADMIN(5),
        ;

        private final int code;

        BaseActionType(int i) {
            this.code = i;
        }
    }
}

