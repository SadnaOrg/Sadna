package AcceptanceTests.DataObjects;

import java.util.HashMap;
import java.util.Map;

public class Shop {
    public int ID;
    public String name;
    public String desc;
    public Map<Integer,ProductInShop> products;
    public User founder;

    public Shop(User founder,int ID, String name, String desc){
        this.founder = founder;
        this.ID = ID;
        this.name = name;
        this.desc = desc;
        products = new HashMap<>();
    }

    public Shop(ServiceLayer.Objects.Shop shop){
        this.name = shop.shopName();
        this.desc = shop.shopDescription();
        this.ID = shop.shopId();
        this.products = new HashMap<>();
        for (ServiceLayer.Objects.Product product:
             shop.shopProducts()) {
            ProductInShop p = new ProductInShop(product);
            products.put(p.ID,p);
        }
    }
}
