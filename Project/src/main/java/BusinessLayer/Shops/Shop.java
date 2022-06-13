package BusinessLayer.Shops;


import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Shops.Polices.Discount.*;
import BusinessLayer.Shops.Polices.Purchase.LogicPurchasePolicy;
import BusinessLayer.Shops.Polices.Purchase.PurchaseAndPolicy;
import BusinessLayer.Shops.Polices.Purchase.PurchasePolicy;
import BusinessLayer.Users.*;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Shop {

    private final int id;
    private String name;
    private String description;
    private State state = State.OPEN;
    private ShopOwner founder;
    private ConcurrentHashMap<Integer, Product> products = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Basket> usersBaskets = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,PurchaseHistory> purchaseHistory= new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ShopAdministrator> shopAdministrators = new ConcurrentHashMap<>();
    private DiscountPlusPolicy discounts = new DiscountPlusPolicy();
    private PurchaseAndPolicy purchasePolicy = new PurchaseAndPolicy();

    public Shop(int id, String name, String description, SubscribedUser founder) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.founder = new ShopOwner(this, founder,founder.getUserName(), true);
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

    public synchronized boolean open() {
        if(state!=State.OPEN){
            state=State.OPEN;
            return true;
        }
        return false;
    }

    public void removeAdmin(String userName) {
        shopAdministrators.remove(userName);
    }

    public void removeBasket(String userName) {
        Basket b = usersBaskets.getOrDefault(userName,null);
        if(b == null){
            throw new IllegalStateException("the user doesn't have a basket!");
        }
        usersBaskets.remove(userName);
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
                old_product.setDescription(new_product.getDescription());
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
        return products;
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
                if (products.containsKey(productID)) {
                    int quantity = usersBaskets.get(user).getProducts().get(productID);
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
            totalPrice -= calculateDiscount(user);
            if(totalPrice<=0)
                return 0;
        }
        else
        {
            throw new IllegalStateException("The shop is closed");
        }
        return totalPrice;
    }

    //we can assume that this function is only called when all good;
    public double checkIfcanBuy(String user) {
        double totalPrice = 0;
        if (state == State.OPEN) {
            for (int productID : usersBaskets.get(user).getProducts().keySet()) {
                if (products.containsKey(productID)) {
                    int quantity = usersBaskets.get(user).getProducts().get(productID);
                    Product curr_product = products.get(productID);
                    double currentPrice = curr_product.checkIfCanBuy(quantity);
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
            totalPrice -= calculateDiscount(user);
            if(totalPrice<=0)
                return 0;
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


    public boolean approvePurchase(User user)
    {
        return this.purchasePolicy.isValid(user,usersBaskets.get(user.getName()));
    }
    public double calculateDiscount(String user){
        return this.discounts.calculateDiscount(usersBaskets.get(user));
    }

    public int addDiscount(int addToConnectId, DiscountRules discountRules) {
        int id = -1;
        if (state == State.OPEN) {
            NumericDiscountRules numericDiscountRule =discounts.getNumericRule(addToConnectId);
            if (numericDiscountRule != null){
                numericDiscountRule.add(discountRules);
                id = discountRules.getID(); // this will return either the connectID or the discountID
                                              // depending on the rule. composite discount -> connectID , leaf discount -> discountID
            }
            else
                throw new IllegalStateException("do not have or can't add discount policy to that Discount Rules");
        }
        else
        {
            throw new IllegalStateException("The shop is closed");
        }
        return id;
    }

    public int addPredicate(int addToConnectId, DiscountPred discountPred) {
        int id = -1;
        if (state == State.OPEN) {
            LogicDiscountRules logicDiscountRules =discounts.getLogicRule(addToConnectId);
            if (logicDiscountRules != null){
                logicDiscountRules.add(discountPred);
                id = discountPred.getID();
            }
            else
                throw new IllegalStateException("do not have or can't add discount predicate to that Discount Rules");
        }
        else
        {
            throw new IllegalStateException("The shop is closed");
        }
        return id;
    }

    public boolean removeDiscount(int ID) {
        if (state == State.OPEN) {
            return discounts.removeSonDiscount(ID);
        }
        else
        {
            throw new IllegalStateException("The shop is closed");
        }
    }

    public boolean removePredicate(int ID) {
        if (state == State.OPEN) {
            return discounts.removeSonPredicate(ID);
        }
        else
        {
            throw new IllegalStateException("The shop is closed");
        }
    }


    public int addPurchasePolicy(int addToConnectId, PurchasePolicy purchasePolicy) {
        int id = -1;
        if (state == State.OPEN) {
            LogicPurchasePolicy logicPurchasePolicyo =purchasePolicy.getLogicRule(addToConnectId);
            if (logicPurchasePolicyo != null){
                logicPurchasePolicyo.add(purchasePolicy);
                id = purchasePolicy.getID();
            }
            else
                throw new IllegalStateException("do not have or can't add discount predicate to that Discount Rules");
        }
        else
        {
            throw new IllegalStateException("The shop is closed");
        }
        return id;
    }

    public boolean removePurchasePolicy(PurchasePolicy purchasePolicyToDelete) {
        if (state == State.OPEN) {
            return purchasePolicy.removeChild(purchasePolicyToDelete);
        }
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
        return purchaseHistory.values();
    }

    public void addPurchaseHistory(String username, PurchaseHistory ph){
        purchaseHistory.put(username, ph);
    }
  
    public Collection<ShopAdministrator> getShopAdministrators() {
        return shopAdministrators.values();
    }

    public boolean isOpen(){
        return state==State.OPEN;
    }

    public DiscountRules getDiscounts() {
        return discounts;
    }

    public PurchasePolicy getPurchasePolicy() {
        return purchasePolicy;
    }

    //    public void addDiscountProductByQuantityDiscount(int productId, int productQuantity, double discount)
//    {
//        if (state == State.OPEN) {
//            if (products.containsKey(productId)) {
//                if(productQuantity<0 || discount<=0 ||discount>1)
//                {
//                    throw new IllegalStateException("The quantity or discount not in the right form");
//                }
//                discountPolicyInterface = new ProductByQuantityDiscount(discountPolicyInterface, productId, productQuantity, discount);
//            }
//            else
//            {
//                throw new IllegalStateException("The product is not in the shop");
//            }
//        }
//        else
//        {
//            throw new IllegalStateException("The shop is closed");
//        }
//    }
//
//    public void addDiscountProductDiscount(int productId, double discount)
//    {
//        if (state == State.OPEN) {
//            if (products.containsKey(productId)) {
//                if(discount<=0 ||discount>1)
//                {
//                    throw new IllegalStateException("The quantity or discount not in the right form");
//                }
//                discountPolicyInterface = new ProductDiscount(discountPolicyInterface, productId, discount);
//            }
//            else
//            {
//                throw new IllegalStateException("The product is not in the shop");
//            }
//        }
//        else
//        {
//            throw new IllegalStateException("The shop is closed");
//        }
//    }
//
//    public void addDiscountProductQuantityInPriceDiscount(int productID, int quantity, double priceForQuantity)
//    {
//        if (state == State.OPEN) {
//            if (products.containsKey(productID)) {
//                if (quantity < 0 || priceForQuantity <= 0) {
//                    throw new IllegalStateException("The quantity or discount not in the right form");
//                }
//                discountPolicyInterface = new ProductQuantityInPriceDiscount(discountPolicyInterface, productID, quantity, priceForQuantity);
//            } else {
//                throw new IllegalStateException("The product is not in the shop");
//            }
//        } else {
//            throw new IllegalStateException("The shop is closed");
//        }
//    }
//
//
//    public void addDiscountRelatedGroupDiscount(Collection < Integer > relatedProducts,double discount)
//    {
//        if (state == State.OPEN) {
//            for(int productId: relatedProducts) {
//                if (products.containsKey(productId)) {
//                    throw new IllegalStateException("The product is not in the shop");
//                }
//            }
//            if (discount <= 0 || discount > 1) {
//                    throw new IllegalStateException("The quantity or discount not in the right form");
//                }
//                discountPolicyInterface = new RelatedGroupDiscount(discountPolicyInterface, relatedProducts, discount);
//
//        } else {
//            throw new IllegalStateException("The shop is closed");
//        }
//    }
//
//    public void addDiscountShopDiscount(int basketQuantity,double discount)
//    {
//        if (state == State.OPEN) {
//                if(basketQuantity<0 || discount<=0 ||discount>1)
//            {
//                throw new IllegalStateException("The quantity or discount not in the right form");
//            }
//            discountPolicyInterface = new ShopDiscount(discountPolicyInterface, basketQuantity, discount);
//        }
//        else
//        {
//            throw new IllegalStateException("The shop is closed");
//        }
//    }
//
//    public void removeDiscountById(int id) {
//        discountPolicyInterface = discountPolicyInterface.removeDiscountById(id);
//    }
//
//    public DiscountPolicyInterface getDiscountById(int id)
//    {
//        return discountPolicyInterface.getDiscountById(id);
//    }

}
