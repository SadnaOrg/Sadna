package BusinessLayer.Notifications;

import BusinessLayer.Notifications.Notification;
import BusinessLayer.Users.UserController;

import java.util.ArrayList;
import java.util.Collection;

public abstract class ConcreteNotification implements Notification {
    private Collection<String> usernames;
    private String content;
    public ConcreteNotification(String content){
        usernames = new ArrayList<>();
        this.content = content;
    }

    @Override
    public boolean notifyUser(String username) {
        if(UserController.getInstance().getUser(username).isLoggedIn()){
            //TODO: notify user
            usernames.remove(username);
        }
        return false;
    }

    @Override
    public boolean contains(String username) {
        return usernames.contains(username);
    }

    @Override
    public boolean remove(String username) {
        return usernames.remove(username);
    }

    @Override
    public boolean add(String username) {
        if(usernames.contains(username))
            return false;
        return usernames.add(username);
    }

    @Override
    public boolean isEmpty(){
        return usernames.isEmpty();
    }
}
