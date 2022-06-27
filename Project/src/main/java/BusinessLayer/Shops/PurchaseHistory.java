package BusinessLayer.Shops;

import BusinessLayer.Notifications.ConcreteNotification;
import BusinessLayer.System.System;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;


public class PurchaseHistory {
    Shop shop;
    String user;
    Collection<Purchase> past_purchases;
    public PurchaseHistory(Shop shop, String user)
    {
        this.shop = shop;
        this.user= user;
        this.past_purchases = new ArrayList<>();
    }

    public PurchaseHistory(Shop shop, String user, Collection<Purchase> purchases){
        this.shop = shop;
        this.user = user;
        this.past_purchases = purchases;
    }

    public void makePurchase()
    {
        Purchase purchase = new Purchase(shop.getId(), user, past_purchases.size()+1,shop.getUsersBaskets().get(user));
        var notifier =System.getInstance().getNotifier();
        var admins = shop.getShopAdministrators().stream().map(sa->sa.getUserName()).collect(Collectors.toList());
        for (var p :shop.getUsersBaskets().get(user).getProducts().entrySet()) {
            notifier.addNotification(new ConcreteNotification(admins,user+" has buy from your shop \""+shop.getName()+"\" "+p.getValue()+" for product #"+p.getKey()+" "+shop.getProducts().get(p.getKey()).getName()));
        }
        past_purchases.add(purchase);
    }

    public Shop getShop() {
        return shop;
    }

    public String getUser() {
        return user;
    }

    public Collection<Purchase> getPast_purchases() {
        return past_purchases;
    }

}
