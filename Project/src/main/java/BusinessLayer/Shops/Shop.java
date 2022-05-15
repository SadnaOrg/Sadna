package BusinessLayer.Shops;


import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.ShopAdministrator;
import BusinessLayer.Users.ShopOwner;
import BusinessLayer.Users.SubscribedUser;

import javax.naming.NoPermissionException;
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
    private ShopOwner founder;
    private ConcurrentHashMap<Integer, Product> products = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Basket> usersBaskets = new ConcurrentHashMap<>();
    private Collection<PurchaseHistory> purchaseHistory= new ArrayList<>();
    private Map<String, ShopAdministrator> shopAdministrators = new ConcurrentHashMap<>();


    public Shop(int id, String name, SubscribedUser founder) {
        this.id = id;
        this.name = name;
        this.founder = new ShopOwner(this, founder, true);
        shopAdministrators.put(founder.getName(),this.founder);
        founder.addAdministrator(id, this.founder);
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
            else
            {
                throw new IllegalStateException("The product is already in the shop");
            }
        }
        else
        {
            throw new IllegalStateException("The shop is closed");
        }
    }

    public void changeProduct(Product new_product) {
        if (state == State.OPEN) {
            if (products.containsKey(new_product.getID())) {
                Product old_product = products.get(new_product.getID());
                old_product.setPrice(new_product.getPrice());
                old_product.setQuantity(new_product.getQuantity());
                old_product.setName(new_product.getName());
            }
            else
            {
                throw new IllegalStateException("The product is not in the shop");
            }
        }
        else
        {
            throw new IllegalStateException("The shop is closed");
        }
    }

    public void removeProduct(int productid) {
        if (state == State.OPEN) {
            if(products.containsKey(productid)) {
                products.remove(productid);
            }
            else
            {
                throw new IllegalStateException("The product is not in the shop");
            }
        }
        else
        {
            throw new IllegalStateException("The shop is closed");
        }
    }

    public ConcurrentHashMap<Integer, Product> getProducts() {
        if (state == State.OPEN)
            return products;
        else
        {
            throw new IllegalStateException("The shop is closed");
        }
    }

    public Collection<Product> searchProducts(ProductFilters pred)
    {
        if (state == State.OPEN)
            return products.values().stream().filter(pred).collect(Collectors.toList());
        else
        {
            throw new IllegalStateException("The shop is closed");
        }
    }

    public double purchaseBasket(String user) {
        int totalPrice = 0;
        if (state == State.OPEN) {
            for (int productID : usersBaskets.get(user).getProducts().keySet()) {
                int quantity = usersBaskets.get(user).getProducts().get(productID);
                if (products.containsKey(productID)) {
                    Product curr_product = products.get(productID);
                    double currentPrice = curr_product.purchaseProduct(quantity);
                    if (currentPrice > 0.0)
                        totalPrice += currentPrice;
                    else
                    {
                        throw new IllegalStateException("Try to buy out of stock product from the shop");
                    }
                }
                else
                {
                    throw new IllegalStateException("The product is not in the shop");
                }
            }
        }
        else
        {
            throw new IllegalStateException("The shop is closed");
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
        {
            throw new IllegalStateException("The shop is closed");
        }
    }

    public ConcurrentHashMap<String, Basket> getUsersBaskets() {
        if (state == State.OPEN)
            return usersBaskets;
        else
        {
            throw new IllegalStateException("The shop is closed");
        }
    }

    public boolean addBasket(String user, Basket basket)
    {
        if (state == State.OPEN) {
            if (!usersBaskets.containsKey(user)) {
                usersBaskets.put(user, basket);
                return true;
            }
            else
            {
                throw new IllegalStateException("The user' basket is already in the shop");
            }
        }
        else
        {
            throw new IllegalStateException("The shop is closed");
        }
    }


    public boolean addAdministrator(String userName,ShopAdministrator administrator) {
        if (state == State.OPEN)
            return shopAdministrators.putIfAbsent(userName,administrator)==null;
        else
        {
            throw new IllegalStateException("The shop is closed");
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    
    public ShopOwner getFounder() { return founder; }

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
