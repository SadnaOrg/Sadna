package ORM.Users;


import ORM.Shops.Shop;

import javax.persistence.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "SubscribedUser")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class SubscribedUser extends User{
    protected String password;

    protected String date;

    protected boolean is_login;
    protected boolean isNotRemoved;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    protected List<ShopAdministrator> administrators;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "userBaskets",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = {
                    @JoinColumn(name = "basketShopID", referencedColumnName = "shopID"),
                    @JoinColumn(name = "basketOwner", referencedColumnName = "username")
            }
    )
    @MapKeyJoinColumn(name = "shop_id")
    protected Map<Shop,Basket> userBaskets;

    public SubscribedUser(String username, String password, String date, boolean is_login, boolean isNotRemoved, PaymentMethod paymentMethod) {
        super(username,paymentMethod);
        this.password = password;
        this.date = date;
        this.is_login = is_login;
        this.isNotRemoved = isNotRemoved;
        this.administrators = new LinkedList<>();
        this.userBaskets = new HashMap<>();
    }

    public SubscribedUser(String username, PaymentMethod paymentMethod, String password, String date, boolean is_login, boolean isNotRemoved, List<ShopAdministrator> administrators, Map<Shop, Basket> userBaskets) {
        super(username, paymentMethod);
        this.password = password;
        this.date = date;
        this.is_login = is_login;
        this.isNotRemoved = isNotRemoved;
        this.administrators = administrators;
        this.userBaskets = userBaskets;
    }

    public SubscribedUser(String password, String date, boolean is_login, boolean isNotRemoved, List<ShopAdministrator> administrators, Map<Shop, Basket> userBaskets) {
        this.password = password;
        this.date = date;
        this.is_login = is_login;
        this.isNotRemoved = isNotRemoved;
        this.administrators = administrators;
        this.userBaskets = userBaskets;
    }

//    public SubscribedUser(String username, PaymentMethod paymentMethod, String password, String date, boolean is_login, boolean isNotRemoved, List<ShopAdministrator> administrators, Map<Shop, Basket> userBaskets) {
//        super(username, paymentMethod);
//        this.password = password;
//        this.date = date;
//        this.is_login = is_login;
//        this.isNotRemoved = isNotRemoved;
//        this.administrators = administrators;
//        this.userBaskets = userBaskets;
//    }

    public SubscribedUser(String username, String password, String date, boolean is_login, boolean isNotRemoved, PaymentMethod paymentMethod, List<ShopAdministrator> administrators) {
        super(username, paymentMethod);
        this.password = password;
        this.date = date;
        this.is_login = is_login;
        this.isNotRemoved = isNotRemoved;
        this.administrators = administrators;
        this.userBaskets = new HashMap<>();
    }

    public SubscribedUser(String username, String password, String date, boolean is_login, boolean isNotRemoved, PaymentMethod paymentMethod, List<ShopAdministrator> administrators, Map<Shop, Basket> userBaskets) {
        super(username, paymentMethod);
        this.password = password;
        this.date = date;
        this.is_login = is_login;
        this.isNotRemoved = isNotRemoved;
        this.administrators = administrators;
        this.userBaskets = userBaskets;
    }



    public SubscribedUser(){

    }

    public String getPassword() {
        return password;
    }

    public String getDate() {
        return date;
    }

    public boolean isIs_login() {
        return is_login;
    }

    public boolean isNotRemoved() {
        return isNotRemoved;
    }

    public List<ShopAdministrator> getAdministrators() {
        return administrators;
    }

    public Map<Shop, Basket> getUserBaskets() {
        return userBaskets;
    }

    public void setUserBaskets(Map<Shop, Basket> userBaskets) {
        this.userBaskets = userBaskets;
    }

    public void addAdministrator(ShopAdministrator administrator) {
        administrators.add(administrator);
    }

    public void addBasket(Basket userBasket) {
        userBaskets.put(userBasket.getShop(),userBasket);
    }
}
