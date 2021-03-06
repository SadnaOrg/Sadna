package AcceptanceTests.DataObjects;

import java.util.List;

public abstract class User {
    public String name;
    public List<String> notifications;
    public int numOfNotifications() {
        return notifications.size();
    }

}
