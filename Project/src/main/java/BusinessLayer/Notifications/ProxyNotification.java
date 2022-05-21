package BusinessLayer.Notifications;

import BusinessLayer.Notifications.Notification;
import BusinessLayer.Users.UserController;

public class ProxyNotification implements Notification {
    private Notification notification = null;

    @Override
    public boolean notifyUser(String user) {
        if(notification != null)
            return notification.notifyUser(user);
        return UserController.getInstance().getUser(user).isLoggedIn();
    }

    @Override
    public boolean contains(String username) {
        if(notification != null)
            return notification.contains(username);
        return true;
    }

    @Override
    public boolean remove(String username) {
        if(notification != null)
            return notification.remove(username);
        return true;
    }

    @Override
    public boolean add(String username) {
        if(notification != null)
            return notification.add(username);
        return true;
    }

    @Override
    public boolean isEmpty() {
        if(notification != null)
            return notification.isEmpty();
        return false;
    }
}
