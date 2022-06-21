package com.SadnaORM.ShopImpl.ShopObjects;

import com.SadnaORM.UserImpl.UserObjects.BasketDTO;
import com.SadnaORM.UserImpl.UserObjects.ShopAdministratorDTO;
import com.SadnaORM.UserImpl.UserObjects.ShopOwnerDTO;
import com.SadnaORM.Users.Basket;
import com.SadnaORM.Users.ShopAdministrator;

import java.util.Collection;
import java.util.Map;

public class ShopDTO {

    private int id;

    private String name;

    private String description;

    private ShopOwnerDTO founder;

    private State state = State.OPEN;

    private Collection<ProductDTO> products;

    private Map<String, BasketDTO> usersBaskets;
    private Map<String, PurchaseHistoryDTO> purchaseHistory;
    private Map<String, ShopAdministratorDTO> shopAdministrators;

    public ShopDTO(int id, String name, String description, ShopOwnerDTO founder, State state, Collection<ProductDTO> products,
                   Map<String, BasketDTO> usersBaskets, Map<String, PurchaseHistoryDTO> purchaseHistory,
                   Map<String, ShopAdministratorDTO> shopAdministrators) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.founder = founder;
        this.state = state;
        this.products = products;
        this.usersBaskets = usersBaskets;
        this.purchaseHistory = purchaseHistory;
        this.shopAdministrators = shopAdministrators;
    }

    public ShopDTO() {

    }


    public enum State {
        OPEN,
        CLOSED;




    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public State getState() {
        return state;
    }
    public ShopOwnerDTO getFounder() { return founder; }
    public Collection<ProductDTO> getProducts() {
        return products;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setProducts(Collection<ProductDTO> products) {
        this.products = products;
    }
    public Map<String, PurchaseHistoryDTO> getPurchaseHistory() {
        return purchaseHistory;
    }

    public Map<String, BasketDTO> getUsersBaskets() {
        return usersBaskets;
    }

    public Map<String, ShopAdministratorDTO> getShopAdministrators() {
        return shopAdministrators;
    }
}
