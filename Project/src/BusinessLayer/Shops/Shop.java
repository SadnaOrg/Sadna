package BusinessLayer.Shops;


import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.ShopAdministrator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
    private Map<String, ShopAdministrator> shopAdministrators = new ConcurrentHashMap<>();


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


    public void addProduct(Product p) {
        if (state == State.OPEN) {
            if(!products.containsKey(p.getID())) {
                products.put(p.getID(), p);
            }
        }
    }

    public void changeProduct(Product new_product) {
        if (products.containsKey(new_product.getID()) && state == State.OPEN) {
            Product old_product = products.get(new_product.getID());
            old_product.setPrice(new_product.getPrice());
            old_product.setQuantity(new_product.getQuantity());
            old_product.setName(new_product.getName());
        }
    }

    public void removeProduct(int productid) {
        if (state == State.OPEN) {
            if(products.containsKey(productid)) {
                products.remove(productid);
            }
        }
    }

    public ConcurrentHashMap<Integer, Product> getProducts() {
        if (state == State.OPEN)
            return products;
        else
            return new ConcurrentHashMap<>();
    }

    public Collection<Product> searchProducts(ProductFilters pred)
    {
        if (state == State.OPEN)
            return products.values().stream().filter(pred).collect(Collectors.toList());
        else
            return new HashSet<>();
    }

    public double purchaseBasket(String user) {
        int totalPrice = 0;
        if (state == State.OPEN) {
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
        }
        return totalPrice;
    }

    public int getId() {
        return id;
    }

    public boolean checkIfUserHasBasket(String user) {
        if (state == State.OPEN)
            return usersBaskets.containsKey(user);
        else
            return false;
    }

    public ConcurrentHashMap<String, Basket> getUsersBaskets() {
        if (state == State.OPEN)
            return usersBaskets;
        else
            return new ConcurrentHashMap<>();
    }

    public boolean addBasket(String user, Basket basket)
    {
        if (state == State.OPEN) {
            usersBaskets.put(user, basket);
            return true;
        }
        else
            return false;
    }


    public boolean addAdministrator(String userName,ShopAdministrator administrator) {
        if (state == State.OPEN)
            return shopAdministrators.putIfAbsent(userName,administrator)==null;
        else
            return false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ShopAdministrator getShopAdministrator(String userName) {
        return shopAdministrators.getOrDefault(userName,null);
    }


    public Collection<PurchaseHistory> getPurchaseHistory() {
        return purchaseHistory;
    }
  
    public Collection<ShopAdministrator> getShopAdministrators() {
        return shopAdministrators.values();
    }

    public boolean isOpen(){
        return state==State.OPEN;
    }
}
