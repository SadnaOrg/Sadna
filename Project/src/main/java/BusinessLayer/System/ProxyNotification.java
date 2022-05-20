package BusinessLayer.System;

import BusinessLayer.Products.Users.UserController;

public class ProxyNotification implements Notification{
    private Notification notification = null;

    @Override
    public boolean notifyUser(String user) {
        if(notification != null)
            return notification.notifyUser(user);
        return UserController.getInstance().getUser(user).isLoggedIn();
    }
}
