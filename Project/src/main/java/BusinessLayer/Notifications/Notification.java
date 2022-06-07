package BusinessLayer.Notifications;

import java.util.Collection;

public interface Notification {
    boolean notifyUser(String username);
    boolean contains(String username);
    boolean remove(String username);
    boolean add(String username);
    boolean isEmpty();

    Collection<String> getUserNames();

    String getContent();
}
