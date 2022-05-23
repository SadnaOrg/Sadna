package ServiceLayer.Objects;


import BusinessLayer.Users.UserController;

import java.util.Map;

public record SubscribedUserInfo(SubscribedUser subscribedUser, UserState state) {

    public SubscribedUserInfo(Map.Entry<UserController.UserState,BusinessLayer.Users.SubscribedUser>e){
        this(new SubscribedUser(e.getValue()),UserState.fromVal(UserController.UserState.getVal(e.getKey())));
    }

}
