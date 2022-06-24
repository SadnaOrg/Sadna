package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.ShopAdministrator;
import BusinessLayer.Users.ShopOwner;
import BusinessLayer.Users.SubscribedUser;

import java.util.Collection;

public class RemoveShopOwner extends RemoveAdmin {
    Shop shop;
    SubscribedUser user;

    public RemoveShopOwner(Shop shop, SubscribedUser user) {
        super(user,shop);
        this.shop = shop;
        this.user = user;
    }

    public boolean act(String userNameToRemove){
        var appoints =user.getAdministrator(shop.getId()).getAppoints();
        if(shop.getShopAdministrator(userNameToRemove) instanceof ShopOwner shopOwnerToRemove){
            return super.act(shopOwnerToRemove.getSubscribed());
        }
        else
            throw new IllegalArgumentException(userNameToRemove+" isn't a owner of the shop");
    }
}
