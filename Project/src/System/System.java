package System;

import Shops.Shop;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class System {
    private Notifier notifier;
    private ExternalServicesSystem externSystem;
    private ConcurrentHashMap<Integer, Shop> shops;

    public void initialize(){
        notifier = new Notifier();
        externSystem = new ExternalServicesSystem();
        shops = new ConcurrentHashMap<>();
    }
}
