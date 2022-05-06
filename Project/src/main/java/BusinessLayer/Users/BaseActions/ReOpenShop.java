package main.java.BusinessLayer.Users.BaseActions;

import main.java.BusinessLayer.Shops.Shop;
import main.java.BusinessLayer.Users.ShopAdministrator;
import main.java.BusinessLayer.Users.ShopOwner;
import main.java.BusinessLayer.Users.SubscribedUser;

public class ReOpenShop extends BaseAction{
    private SubscribedUser user;
    private Shop shop;

    public ReOpenShop(Shop shop, SubscribedUser user){
        this.shop = shop;
        this.user = user;
    }

    public boolean act() throws Exception {
        ShopAdministrator admin = shop.getShopAdministrator(user.getUserName());
        if(admin instanceof ShopOwner && ((ShopOwner) admin).isFounder()){
            if(shop.isOpen()){
                throw new IllegalStateException("can't reopen an open shop");
            }
            else {
                shop.open();
                return true;
            }
        }
        throw new Exception("not a founder has tried to reopen the shop!");
    }
}
