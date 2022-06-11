package BusinessLayer.Notifications;

import BusinessLayer.Users.UserController;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Function;

public class Notifier {
    private final Map<String,Collection<Notification>> delayNotifications;
    private final Map<String, Function<Notification, Boolean>> connection;

    public Notifier() {
        delayNotifications = new ConcurrentHashMap<>();
        connection = new ConcurrentHashMap<>();
    }

    public void addNotification(Notification not){
        for(String user : not.getUserNames()) {
            if (UserController.getInstance().getUser(user).isLoggedIn() && connection.containsKey(user)) {
                connection.get(user).apply(not);
            } else {
                addDelayedNotification(user,not);
            }
        }
    }

    private void addDelayedNotification(String user, Notification not) {
        if(!delayNotifications.containsKey(user))
            delayNotifications.put(user,new ConcurrentLinkedDeque<>());
        delayNotifications.get(user).add(not);
    }

    public void register(Function<Notification,Boolean> con, String userName){
       connection.put(userName,con);
    }
    public void getDelayedNotifications(String userName){
        if(delayNotifications.containsKey(userName))
        for (var not:delayNotifications.replace(userName,new ConcurrentLinkedDeque<>())) {
            connection.get(userName).apply(not);
        }
    }

    public boolean unregister(String userName){
       return connection.remove(userName)!=null;
    }
}
