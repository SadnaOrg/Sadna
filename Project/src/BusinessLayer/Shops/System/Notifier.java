package BusinessLayer.Shops.System;

import java.util.concurrent.ConcurrentHashMap;

public class Notifier {
    private ConcurrentHashMap<Integer, Notification> notifications;

    public Notifier(){
        notifications = new ConcurrentHashMap<>();
    }
}
