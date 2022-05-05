package AcceptanceTests.DataObjects;

import java.util.HashMap;
import java.util.Map;

public class Shop {
    public int ID;
    public String name;
    public double rating;
    public String category;
    public Map<Integer,ProductInShop> products;
    public User founder;

    public Shop(User founder,int ID, String name, double rating, String category){
        this.founder = founder;
        this.ID = ID;
        this.name = name;
        this.rating = rating;
        this.category = category;
        products = new HashMap<>();
    }
}
