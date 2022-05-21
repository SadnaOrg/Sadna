package BusinessLayer.Notifications;

import BusinessLayer.Users.UserController;

import java.util.ArrayList;
import java.util.Collection;

public class Notifier {
    private Collection<Notification> notifications;

    public Notifier(){
        notifications = new ArrayList<>();
    }

    private synchronized void addNotification(Notification notification){
        notifications.add(notification);
    }

    public void notifyUser(String username){
        for(Notification notif : notifications){
            if(notif.contains(username)){
                //TODO: send user notification
                notif.remove(username);
                if(notif.isEmpty()){
                    notifications.remove(notif);
                }
            }
        }
    }

    public void addNotification(Collection<String> usernames, String content){
        RealTimeNotification realNotif = new RealTimeNotification(content);
        PausedNotification pausedNotif = new PausedNotification(content);
        for(String user : usernames) {
            if (UserController.getInstance().getUser(user).isLoggedIn()) {
                realNotif.add(user);
            } else {
                pausedNotif.add(user);
            }
        }
        if(!realNotif.isEmpty())
            addNotification(realNotif);
        if(!pausedNotif.isEmpty())
            addNotification(pausedNotif);
    }

    public Collection<Notification> getNotifications() {
        return notifications;
    }
}
