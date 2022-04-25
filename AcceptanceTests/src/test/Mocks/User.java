package test.Mocks;

import java.util.List;

public abstract class User {
    public int ID;
    public List<String> notifications;
    public int numOfNotifications() {
        return notifications.size();
    }

}
