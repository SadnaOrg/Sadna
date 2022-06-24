package ORM.Users;

import ORM.Shops.Shop;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "basket")
@IdClass(Basket.BasketPKID.class)
public class Basket {
    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shopID")
    private Shop shop;
    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private SubscribedUser user;

    @ElementCollection
    @CollectionTable(
            name = "ProductsInBasketsQuantities",
            joinColumns = {
                    @JoinColumn(name = "shopID", referencedColumnName = "shopID"),
                    @JoinColumn(name = "username", referencedColumnName = "username")
            }
    )
    @MapKeyColumn(name = "PRODUCT_ID")
    @Column(name = "QUANTITY")
    private Map<Integer,Integer> products;
    @ElementCollection
    @CollectionTable(
            name = "ProductsInBasketsPrices",
            joinColumns = {
                    @JoinColumn(name = "shopID", referencedColumnName = "shopID"),
                    @JoinColumn(name = "username", referencedColumnName = "username")
            }
    )
    @MapKeyColumn(name = "PRODUCT_ID")
    @Column(name = "PRICE")
    private Map<Integer,Double> prices;
    @ElementCollection
    @CollectionTable(
            name = "ProductsInBasketsCategories",
            joinColumns = {
                    @JoinColumn(name = "shopID", referencedColumnName = "shopID"),
                    @JoinColumn(name = "username", referencedColumnName = "username")
            }
    )
    @MapKeyColumn(name = "PRODUCT_ID")
    @Column(name = "CATEGORY")
    private Map<Integer,String> categories;


    public Basket(Shop shop, SubscribedUser user, Map<Integer, Integer> products, Map<Integer, Double> prices, Map<Integer, String> categories) {
        this.shop = shop;
        this.user = user;
        this.products = products;
        this.prices = prices;
        this.categories = categories;
    }

    public Basket() {
    }

    public Basket(Shop shop, SubscribedUser user, Map<Integer, Integer> products) {
        this.shop = shop;
        this.user = user;
        this.products = products;
    }

    public Basket(Shop shop, SubscribedUser user){
        this.shop = shop;
        this.user = user;
        this.products = new HashMap<>();
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void setUser(SubscribedUser user) {
        this.user = user;
    }

    public Map<Integer, Double> getPrices() {
        return prices;
    }

    public void setPrices(Map<Integer, Double> prices) {
        this.prices = prices;
    }

    public Map<Integer, String> getCategories() {
        return categories;
    }

    public void setCategories(Map<Integer, String> categories) {
        this.categories = categories;
    }

    public void setProducts(Map<Integer, Integer> products) {
        this.products = products;
    }

    public Shop getShop() {
        return shop;
    }

    public SubscribedUser getUser() {
        return user;
    }

    public Map<Integer,Integer> getProducts() {
        return products;
    }

    public static class BasketPKID implements Serializable {
        private Shop shop;
        private SubscribedUser user;

        public BasketPKID(Shop shop, SubscribedUser user) {
            this.shop = shop;
            this.user = user;
        }

        public BasketPKID(){

        }
    }
}
