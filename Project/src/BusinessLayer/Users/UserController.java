package BusinessLayer.Users;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserController {
    private final Map<String,User> users;

    public UserController() {
        users = new ConcurrentHashMap<>();
    }


}
