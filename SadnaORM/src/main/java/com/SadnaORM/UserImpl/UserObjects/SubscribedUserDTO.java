package com.SadnaORM.UserImpl.UserObjects;

import com.SadnaORM.ShopImpl.ShopObjects.ShopDTO;
import com.SadnaORM.Shops.Shop;
import com.SadnaORM.Users.PaymentMethod;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SubscribedUserDTO extends UserDTO {
    protected String password;
    protected boolean is_login;
    protected boolean isNotRemoved;
    protected List<ShopAdministratorDTO> administrators;
    protected Map<Integer, BasketDTO> userBaskets;

    public SubscribedUserDTO(String username, String password, boolean is_login, boolean isNotRemoved, PaymentMethodDTO paymentMethod) {
        super(username,paymentMethod);
        this.password = password;
        this.is_login = is_login;
        this.isNotRemoved = isNotRemoved;
        this.administrators = new LinkedList<>();
        this.userBaskets = new HashMap<>();
    }

    public SubscribedUserDTO(String username, String password, boolean is_login, boolean isNotRemoved, PaymentMethodDTO paymentMethod, List<ShopAdministratorDTO> administrators) {
        super(username, paymentMethod);
        this.password = password;
        this.is_login = is_login;
        this.isNotRemoved = isNotRemoved;
        this.administrators = administrators;
        this.userBaskets = new HashMap<>();
    }

    public SubscribedUserDTO(String username, String password, boolean is_login, boolean isNotRemoved, PaymentMethodDTO paymentMethod, List<ShopAdministratorDTO> administrators, Map<Integer, BasketDTO> userBaskets) {
        super(username, paymentMethod);
        this.password = password;
        this.is_login = is_login;
        this.isNotRemoved = isNotRemoved;
        this.administrators = administrators;
        this.userBaskets = userBaskets;
    }

    public SubscribedUserDTO(){

    }

    public String getPassword() {
        return password;
    }

    public boolean isIs_login() {
        return is_login;
    }

    public boolean isNotRemoved() {
        return isNotRemoved;
    }

    public List<ShopAdministratorDTO> getAdministrators() {
        return administrators;
    }

    public Map<Integer, BasketDTO> getUserBaskets() {
        return userBaskets;
    }

    public void addAdministrator(ShopAdministratorDTO administrator) {
        administrators.add(administrator);
    }
}
