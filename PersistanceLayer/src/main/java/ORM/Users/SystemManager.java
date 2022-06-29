package ORM.Users;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class SystemManager extends SubscribedUser{
    public SystemManager() {
    }

    public SystemManager(String username, String password, String date, boolean is_login, boolean isNotRemoved, PaymentMethod paymentMethod){
        super(username, password, date, is_login, isNotRemoved, paymentMethod);

    }

    public SystemManager(String username, String password, String date, boolean is_login, boolean isNotRemoved, PaymentMethod paymentMethod, List<ShopAdministrator> administrators){
        super(username, password, date, is_login, isNotRemoved, paymentMethod, administrators);
    }
}
