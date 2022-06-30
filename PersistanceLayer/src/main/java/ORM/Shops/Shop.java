package ORM.Shops;
import ORM.Users.Basket;
import ORM.Users.ShopAdministrator;
import ORM.Users.ShopOwner;
import ORM.Users.SubscribedUser;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    private ShopOwner founder;

    @Enumerated(EnumType.STRING)
    private State state = State.OPEN;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Collection<Product> products;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "shopBaskets",
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

    public Map<SubscribedUser, HeskemMinui> getMinuiMap() {
        return minuiMap;
    }

    public void setMinuiMap(Map<SubscribedUser, HeskemMinui> minuiMap) {
        this.minuiMap = minuiMap;
    }

    public Shop(int id, String name, String description, ShopOwner founder, State state, Collection<Product> products, Map<SubscribedUser, Basket> usersBaskets, Map<SubscribedUser, PurchaseHistory> purchaseHistory, Map<SubscribedUser, ShopAdministrator> shopAdministrators, Map<SubscribedUser, HeskemMinui> minuiMap) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.founder = founder;
        this.state = state;
        this.products = products;
        this.usersBaskets = usersBaskets;
        this.purchaseHistory = purchaseHistory;
        this.shopAdministrators = shopAdministrators;
        this.minuiMap = minuiMap;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "MinuimInShop",
    joinColumns = {@JoinColumn(name = "shopID", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "HeskemShop",referencedColumnName = "shop_id"),
    @JoinColumn(name = "assignee", referencedColumnName = "adminToAssign_username")})
    @MapKeyJoinColumn(name = "adminToAssign_username")
    private Map<SubscribedUser, HeskemMinui> minuiMap;

    public Shop(String name, String description, SubscribedUser founder, boolean isFounder, State state, Collection<Product> products,
                Map<SubscribedUser, Basket> usersBaskets, Map<SubscribedUser, PurchaseHistory> purchaseHistory,
                Map<SubscribedUser, ShopAdministrator> shopAdministrators) {
        this.name = name;
        this.description = description;
        this.founder = new ShopOwner(new ArrayList<>(), founder, this, isFounder);
        this.state = state;
        this.products = products;
        this.usersBaskets = usersBaskets;
        this.purchaseHistory = purchaseHistory;
        this.shopAdministrators = shopAdministrators;
    }
    public Shop(String name, String description) {
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
    public void setId(int id) {
        this.id = id;
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
    public ShopOwner getFounder() {
        return founder;
    }
    public void setFounder(ShopOwner founder) {
        this.founder = founder;
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
