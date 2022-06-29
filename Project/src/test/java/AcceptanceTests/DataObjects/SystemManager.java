package AcceptanceTests.DataObjects;

public class SystemManager extends SubscribedUser{
    public SystemManager(String username) {
        super(username);
    }

    public SystemManager(ServiceLayer.Objects.SubscribedUser user) {
        super(user);
    }
}
