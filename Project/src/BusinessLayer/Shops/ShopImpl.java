package Shops;

import Users.SubscribedUser;

import java.util.ArrayList;

public class ShopImpl implements Shop {


    public enum State {
        OPEN,
        CLOSED
    }
    private int id;
    private String name;
    private State state;
    private ArrayList<SubscribedUser> owners = new ArrayList<>();
    private SubscribedUser founder;


    public SubscribedUser getFounder() {
        return founder;
    }

    public void addShopOwner(SubscribedUser su) {
        owners.add(su);
    }
}
