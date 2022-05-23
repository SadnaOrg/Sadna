package ServiceLayer.Objects;

import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.UserController;

public enum UserState{
    REMOVED,LOGGED_IN,LOGGED_OUT;

    static UserState fromVal(int val){
        return switch (val){
            case -1 -> REMOVED;
            case 1 -> LOGGED_IN;
            default -> LOGGED_OUT;
        };
    }
}
