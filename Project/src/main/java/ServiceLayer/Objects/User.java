package ServiceLayer.Objects;

public class User {
    public String username;
    public User(BusinessLayer.Products.Users.User user){
        this.username = user.getUserName();
    }
}
