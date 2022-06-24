package BusinessLayer.Shops;


import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.User;
import BusinessLayer.Users.UserController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ShopController {


    public double getProductPrice(int shopId, int productId) {
        Shop s = shops.getOrDefault(shopId,null);
        if(s != null){
            Product product = s.getProducts().getOrDefault(productId,null);
            if (product != null)
                return product.getPrice();
            return -1;
        }
        return -1;
    }

    public void removeBaskets(List<Integer> IDs, String userName) {
        for (int shopID:
             IDs) {
            Shop s = shops.getOrDefault(shopID,null);
            if(s == null)
                throw new IllegalStateException("no such shop!");
            s.removeBasket(userName);
        }
    }

    public void tryRemove(int shopID,String username,int newQuantity){
        if(newQuantity != 0)
            return;
        if(checkIfUserHasBasket(shopID,username)){
            Shop s = shops.getOrDefault(shopID,null);
            if(s == null)
                throw new IllegalStateException("no such shop!");

            Basket b = s.getUsersBaskets().get(username);
            if(b.getProducts().size() == 0)
                s.removeBasket(username);
        }
    }

    static private class ShopControllerHolder {
        static final ShopController sc = new ShopController();
    }

    public static ShopController getInstance() {
        return ShopControllerHolder.sc;
    }


    private final Map<Integer, Shop> shops;

    private ShopController() {
        this.shops = new ConcurrentHashMap<>();
    }

    private Map<Integer, Shop> loadFromDB() {
        return ShopMapper.getInstance().findAll().stream()
                .collect(Collectors.toMap(Shop::getId, Function.identity()));
    }

    public Map<Shop, Collection<Product>> searchProducts(ShopFilters shopPred, ProductFilters productPred) {
        Map<Shop, Collection<Product>> res = new ConcurrentHashMap<>();
        for (Shop s : shops.values().stream().filter(shopPred.and(Shop::isOpen)).collect(Collectors.toSet())) {
            res.put(s, s.searchProducts(productPred));
        }
        return res;
    }

    public ConcurrentHashMap<Integer, Double> purchaseBasket(String user) {
        ConcurrentHashMap<Integer, Double> finalprices = new ConcurrentHashMap<>();
        for (int shopid : shops.keySet()) {
            try {
                if(checkIfUserHasBasket(shopid,user))
                    finalprices.put(shopid, shops.get(shopid).checkIfcanBuy(user));
            }
            catch (IllegalStateException e)
            {
                //TODO: add notification when implemented
                //finalprices.put(shopid,0.0);
            }
        }
        if(finalprices.size() == 0)
            throw new IllegalStateException("can't purchase an empty cart!");
        return finalprices;
    }

    public boolean addToPurchaseHistory(String user, ConcurrentHashMap<Integer, Boolean> paymentSituation) {
        for (int shopid:paymentSituation.keySet())
        {
            if(paymentSituation.get(shopid))
            {
                boolean flagexist= false;
                for (PurchaseHistory purchaseHistory:PurchaseHistoryController.getInstance().getDataOnPurchases())
                {
                    if(purchaseHistory.getShop().getId()==shopid && purchaseHistory.getUser().equals(user)) {
                        purchaseHistory.makePurchase();
                        flagexist= true;
                    }
                }
                if(!flagexist)
                {
                    PurchaseHistory purchaseHistory = PurchaseHistoryController.getInstance().createPurchaseHistory(shops.get(shopid), user);
                    purchaseHistory.makePurchase();
                    shops.get(shopid).addPurchaseHistory(user, purchaseHistory);
                }
                shops.get(shopid).purchaseBasket(user);
                shops.get(shopid).getUsersBaskets().remove(user);
                UserController.getInstance().getShoppingCart(user).remove(shopid);
            }
        }
        return true;
    }

    public boolean checkIfUserHasBasket(int shopid, String user) {
        if(shops.containsKey(shopid))
            return shops.get(shopid).checkIfUserHasBasket(user);
        return false;
    }

    public boolean AddBasket(int shopid, String user, Basket basket) {
        return shops.get(shopid).addBasket(user,basket);
    }

    public Map<Integer, Shop> getShops() {
        return shops;
    }

    public boolean addShop(Shop s1) {
        if (shops.containsKey(s1.getId())) {
            return false;
        }
        else {
            shops.put(s1.getId(), s1);
            return true;
        }
    }

    public ConcurrentHashMap<Integer,ShopInfo> reciveInformation() {
        ConcurrentHashMap<Integer,ShopInfo> shopsInfo= new ConcurrentHashMap<>();
        for (Shop s:shops.values())
        {
            if (s.isOpen())
                shopsInfo.put(s.getId(),new ShopInfo(s));
        }
        return shopsInfo;
    }

    public Shop openShop(SubscribedUser su, String name, String description) {
//        List<String> names = shops.values().stream().map(Shop::getName).toList();
//        if(names.contains(name))
//            throw new IllegalStateException("there is a shop with that name!!!");
        int shopID = shops.size();
        Shop shop = new Shop(shopID, name, description, su);
        shops.put(shopID, shop);
        ShopMapper.getInstance().save(shop);
        return shops.get(shopID);
    }


    public double getCartPrice(User user) {
        double finalprice =0;
        for (int shopid : shops.keySet()) {
            finalprice +=getBasketPrice(shopid,user);
        }
        return finalprice;
    }

    public double getBasketPrice(int shopid , User user) {
        try {
            if (checkIfUserHasBasket(shopid, user.getUserName())) {
                //added here
                if (shops.get(shopid).approvePurchase(user))
                    return shops.get(shopid).checkIfcanBuy(user.getUserName());
            }
        }
        catch (IllegalStateException ignored)
        {
        }
        return 0;
    }

    public ConcurrentHashMap<Integer, ShopInfo> searchShops(ShopFilters shopPred, String username) {
        ConcurrentHashMap<Integer, ShopInfo> res = new ConcurrentHashMap<>();
        for (Shop s : shops.values().stream().filter(s -> shopPred.test(s) && s.getShopAdministrators().stream().filter(a -> Objects.equals(a.getUserName(), username)).toList().size() > 0).collect(Collectors.toSet())) {
            res.put(s.getId(), new ShopInfo(s));
        }
        return res;
    }

    public ConcurrentHashMap<Integer, Double> purchaseBasket(User user) {
        ConcurrentHashMap<Integer, Double> finalprices = new ConcurrentHashMap<>();
        for (int shopid : shops.keySet()) {
            try {
                if (checkIfUserHasBasket(shopid, user.getUserName())) {
                    //added here
                    if (shops.get(shopid).approvePurchase(user))
                        finalprices.put(shopid, shops.get(shopid).checkIfcanBuy(user.getUserName()));
                }
            }
            catch (IllegalStateException ignored)
            {
            }
        }
        if(finalprices.size() == 0)
            throw new IllegalStateException("can't purchase an empty cart!");
        return finalprices;
    }

}
