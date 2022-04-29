package BusinessLayer.Users;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserController {

    static private class UserControllerHolder {
        static final UserController uc = new UserController();
    }

    public static UserController getInstance(){
        return UserControllerHolder.uc;
    }


    private final Map<String,User> users;

    private UserController() {
        users = new ConcurrentHashMap<>();
    }


}
