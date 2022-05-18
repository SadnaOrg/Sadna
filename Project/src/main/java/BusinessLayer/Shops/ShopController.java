package BusinessLayer.Shops;


import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Products.Users.Basket;
import BusinessLayer.Products.Users.SubscribedUser;
import BusinessLayer.Products.Users.UserController;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ShopController {



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
                finalprices.put(shopid, shops.get(shopid).purchaseBasket(user));
            }
            catch (IllegalStateException e)
            {
                //TODO: add notification when implemented
                finalprices.put(shopid,0.0);
            }
        }
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
                shops.get(shopid).getUsersBaskets().remove(user);
                UserController.getInstance().getShoppingCart(user).remove(shopid);
            }
        }
        return true;
    }

    public boolean checkIfUserHasBasket(int shopid, String user) {
        return shops.get(shopid).checkIfUserHasBasket(user);
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
        int shopID = shops.size();
        shops.put(shopID, new Shop(shopID, name, description, su));
        return shops.get(shopID);
    }


}
