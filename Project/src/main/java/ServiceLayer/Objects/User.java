package ServiceLayer.Objects;

public class User {
    public String username;
    public User(BusinessLayer.Users.User user){
        this.username = user.getUserName();
    }
}
