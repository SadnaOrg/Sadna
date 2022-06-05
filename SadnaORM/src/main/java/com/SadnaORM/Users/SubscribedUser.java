package com.SadnaORM.Users;

import com.SadnaORM.Shops.Shop;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "SubscribedUser")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class SubscribedUser extends User{
    protected String password;
    protected boolean is_login;
    protected boolean isNotRemoved;
    @OneToMany(mappedBy = "user")
    protected List<ShopAdministrator> administrators;
    @OneToMany
    @JoinTable(name = "userBaskets",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = {
                    @JoinColumn(name = "basketShopID", referencedColumnName = "shopID"),
                    @JoinColumn(name = "basketOwner", referencedColumnName = "username")
            }
    )
    @MapKeyJoinColumn(name = "shop_id")
    protected Map<Shop,Basket> userBaskets;

    public SubscribedUser(String username, String password, boolean is_login, boolean isNotRemoved, PaymentMethod paymentMethod) {
        super(username,paymentMethod);
        this.password = password;
        this.is_login = is_login;
        this.isNotRemoved = isNotRemoved;
    }

    public SubscribedUser(String username, String password, boolean is_login, boolean isNotRemoved, PaymentMethod paymentMethod, List<ShopAdministrator> administrators) {
        super(username, paymentMethod);
        this.password = password;
        this.is_login = is_login;
        this.isNotRemoved = isNotRemoved;
        this.administrators = administrators;
    }

    public SubscribedUser(){

    }

    public String getUsername(){return this.username;}
}
