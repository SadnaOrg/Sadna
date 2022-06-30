package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Shop;

public abstract class BaseAction{
    protected Shop shop;

    public BaseAction(Shop shop) {
        this.shop = shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
