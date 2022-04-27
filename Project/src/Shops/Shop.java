package Shops;


import Users.SubscribedUser;

public interface Shop {
    SubscribedUser getFounder();
    void addShopOwner(SubscribedUser su);

}
