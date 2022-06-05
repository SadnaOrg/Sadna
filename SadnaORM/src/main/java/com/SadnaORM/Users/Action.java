package com.SadnaORM.Users;


import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Embeddable
public class Action {

    private int actionCode;

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
