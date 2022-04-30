package BusinessLayer.Shops;


import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Users.Basket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Shop {

    private int id;
    private String name;
    private String description;
    private State state = State.OPEN;
    private ConcurrentHashMap<Integer, Product> products = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Basket> usersBaskets = new ConcurrentHashMap<>();
    private Collection<PurchaseHistory> purchaseHistory= new ArrayList<>();

    public Shop(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public synchronized boolean close() {
        if(state!=State.CLOSED){
            state=State.CLOSED;
            return true;
        }
        return false;
    }

    public enum State {
        OPEN,
        CLOSED
    }

    public enum ShopAdministratorType{
        MANAGER,
        OWNER,
        FOUNDER
    }


    private Map<String,ShopAdministratorType> shopAdministrators = new ConcurrentHashMap<>();

    public void addProduct(Product p) {
        products.put(p.getID(), p);
    }

    public void changeProduct(Product new_product) {
        if (products.containsKey(new_product.getID())) {
            Product old_product = products.get(new_product.getID());
            old_product.setPrice(new_product.getPrice());
            old_product.setQuantity(new_product.getQuantity());
            old_product.setName(new_product.getName());
        }
    }

    public void removeProduct(Product p) {
        products.remove(p.getID());
    }

    public ConcurrentHashMap<Integer, Product> getProducts() {
        return products;
    }

    public Collection<Product> searchProducts(ProductFilters pred){
        return products.values().stream().filter(pred).collect(Collectors.toList());
    }

    public double purchaseBasket(String user) {
        int totalPrice = 0;
        for (int productID : usersBaskets.get(user).getProducts().keySet()) {
            int quantity = usersBaskets.get(user).getProducts().get(productID);
            if (products.containsKey(productID)) {
                Product curr_product = products.get(productID);
                double currentPrice = curr_product.purchaseProduct(quantity);
                if (currentPrice == 0.0)
                    return currentPrice;
                else
                    totalPrice += currentPrice;
            }
        }
        return totalPrice;
    }

    public int getId() {
        return id;
    }

    public boolean checkIfUserHasBasket(String user) {
        return usersBaskets.containsKey(user);
    }

    public ConcurrentHashMap<String, Basket> getUsersBaskets() {
        return usersBaskets;
    }

    public boolean addBasket(String user, Basket basket)
    {
        usersBaskets.put(user,basket);
        return true;
    }


    public boolean addAdministrator(String userName,ShopAdministratorType administratorType){
       return shopAdministrators.putIfAbsent(userName,administratorType)!=null;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ShopAdministratorType getShopAdministrator(String userName) {
        return shopAdministrators.getOrDefault(userName,null);
    }

    public Collection<PurchaseHistory> getPurchaseHistory() {
        return purchaseHistory;
    }
}
