package ORM;

import ORM.DAOs.Users.SubscribedUserDAO;
import ORM.Users.SubscribedUser;

public class TempApp {
    public static void main(String[] args) {
        //SubscribedUserDAO dao = new SubscribedUserDAO();
        SubscribedUser user1 = new SubscribedUser("user123", "pass123", false, true, null);
        SubscribedUser user2 = new SubscribedUser("user456", "pass123", false, true, null);

        SubscribedUserDAO dao = new SubscribedUserDAO();
        dao.save(user1);
        dao.save(user2);
        dao.findAll().forEach(u -> System.out.println(u.getUsername()));
    }
}
