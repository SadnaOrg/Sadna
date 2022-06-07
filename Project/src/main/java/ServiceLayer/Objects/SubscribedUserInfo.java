package ServiceLayer.Objects;


import BusinessLayer.Users.UserController;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record SubscribedUserInfo(Collection<SubscribedUser> subscribedUsers, UserState state) {

    public SubscribedUserInfo(Map.Entry<UserController.UserState, List<BusinessLayer.Users.SubscribedUser>>e){
        this(e.getValue().stream().map(SubscribedUser::new).collect(Collectors.toList()), UserState.fromVal(UserController.UserState.getVal(e.getKey())));
    }

}
