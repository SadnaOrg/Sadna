package System;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Notifier {
    private ConcurrentHashMap<Integer, Notification> notifications;

    public Notifier(){
        notifications = new ConcurrentHashMap<>();
    }
}
