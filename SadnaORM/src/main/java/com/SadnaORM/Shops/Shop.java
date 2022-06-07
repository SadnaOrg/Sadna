package com.SadnaORM.Shops;

import com.SadnaORM.Users.Basket;
import com.SadnaORM.Users.ShopAdministrator;
import com.SadnaORM.Users.SubscribedUser;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "shop")
public class Shop {

    @Id
    private int id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private State state = State.OPEN;
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Collection<Product> products;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "userBaskets",
            inverseJoinColumns = {
            @JoinColumn(name = "basketShopID", referencedColumnName = "shopID"),
                    @JoinColumn(name = "basketOwner", referencedColumnName = "username")
    })
    @MapKeyJoinColumn(name = "username")
    private Map<SubscribedUser, Basket> usersBaskets;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "subscribedUserPurchaseHistory",
            joinColumns = {@JoinColumn(name = "shopID", referencedColumnName = "id")})
    @MapKeyJoinColumn(name = "username")
    private Map<SubscribedUser, PurchaseHistory> purchaseHistory;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "AdministratorsOfShops",
            joinColumns = {@JoinColumn(name = "shopID", referencedColumnName = "id")})
    @MapKeyJoinColumn(name = "username")
    private Map<SubscribedUser, ShopAdministrator> shopAdministrators;

    public Shop(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.state = State.OPEN;
        this.products = new ArrayList<>();
        this.usersBaskets = new ConcurrentHashMap<>();
    }

    public Shop() {

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

    public Collection<Product> getProducts() {
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

    public void setProducts(Collection<Product> products) {
        this.products = products;
    }
    public Map<SubscribedUser, PurchaseHistory> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void setPurchaseHistory(Map<SubscribedUser, PurchaseHistory> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    public Map<SubscribedUser, Basket> getUsersBaskets() {
        return usersBaskets;
    }

    public void setUsersBaskets(Map<SubscribedUser, Basket> usersBaskets) {
        this.usersBaskets = usersBaskets;
    }

    public Map<SubscribedUser, ShopAdministrator> getShopAdministrators() {
        return shopAdministrators;
    }

    public void setShopAdministrators(Map<SubscribedUser, ShopAdministrator> shopAdministrators) {
        this.shopAdministrators = shopAdministrators;
    }
}
