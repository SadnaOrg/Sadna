package BusinessLayer.Shops;


import BusinessLayer.Mappers.MapperController;
import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Shops.Polices.Discount.*;
import BusinessLayer.Users.*;

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
            if(b.getProducts().size() == 0) {
                s.removeBasket(username);
                mapperController.getShopMapper().update(shops.get(shopID));
            }
        }
    }

    public ConcurrentHashMap<Integer, ShopInfo> searchShops(ShopFilters shopPred, String username) {
        ConcurrentHashMap<Integer, ShopInfo> res = new ConcurrentHashMap<>();
        Collection<Shop> ormShops = MapperController.getInstance().getShopMapper().findByFounder(username);
        for (Shop shop : ormShops)
            if (!shops.containsKey(shop.getId())) {
                shops.put(shop.getId(), shop);
                UserController.getInstance().getSubUser(username).addAdministrator(shop.getId(), shop.getFounder());
            }
        for (Shop s : shops.values().stream().filter(s -> shopPred.test(s) && s.getShopAdministrators().stream().filter(a -> Objects.equals(a.getUserName(), username)).toList().size() > 0).collect(Collectors.toSet())) {
            res.put(s.getId(), new ShopInfo(s));
        }
        return res;
    }

    static private class ShopControllerHolder {
        static final ShopController sc = new ShopController();
    }

    public static ShopController getInstance() {
        return ShopControllerHolder.sc;
    }


    private final Map<Integer, Shop> shops;
    private MapperController mapperController = MapperController.getInstance();

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

    public boolean removeBid(int shopId)
    {
        return null != shops.get(shopId).getUsersBids().remove(shopId);
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
                        //TODO:need to change back
                        purchaseHistory.makePurchase(shops.get(shopid));
                        flagexist= true;
                    }
                }
                if(!flagexist)
                {
                    PurchaseHistory purchaseHistory = PurchaseHistoryController.getInstance().createPurchaseHistory(shops.get(shopid), user);
                    //TODO:need to change back
                    purchaseHistory.makePurchase(shops.get(shopid));
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

    public boolean checkIfUserHasBid(int shopid, String user) {
        if(shops.containsKey(shopid))
            return shops.get(shopid).checkIfUserHasBid(user);
        return false;
    }

    public boolean AddBasket(int shopid, String user, Basket basket) {
        boolean res = shops.get(shopid).addBasket(user,basket);
        if (res)
            mapperController.getShopMapper().update(shops.get(shopid));
        return res;
    }


    public boolean addBidOffer(int shopid,int productId, String user, BidOffer bidOffer) {
        return shops.get(shopid).addBidOffer(user,productId,bidOffer);
    }
//
//    public boolean reOfferBid(int shopid,String user,int productId, double newPrice)
//    {
//        return shops.get(shopid).reOfferBid(user,productId,newPrice);
//
//    }
//
//    public boolean declineBidOffer(int shopid,String user,int productId)    {
//        if(shops.get(shopid).declineBidOffer(user,productId))
//        {
//            UserController.getInstance().getUser(user).getShoppingBids().remove(shopid);
//        }
//
//    }
//
//    public boolean approveBidOffer(int shopid,String user,String adminName,int productId)
//    {
//        return shops.get(shopid).approveBidOffer(user,adminName,productId);
//
//    }
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
        Shop shop = new Shop(name, description, su);
        int shopID = mapperController.getShopMapper().getInstance().save(shop);
        shop.setId(shopID);
        shops.put(shopID, shop);
        return shops.get(shopID);
    }


}
