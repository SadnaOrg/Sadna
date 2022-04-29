package BusinessLayer.System;

import BusinessLayer.Shops.Shop;

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
