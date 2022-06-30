package ORM.Users;

import ORM.Shops.Shop;

import javax.persistence.Entity;
import java.util.List;
import java.util.Map;

@Entity
public class SystemManager extends SubscribedUser{
    public SystemManager() {
    }

    public SystemManager(String username, String password, String date, boolean is_login, boolean isNotRemoved, PaymentMethod paymentMethod){
        super(username, password, date, is_login, isNotRemoved, paymentMethod);

    }

    public SystemManager(String username, PaymentMethod paymentMethod, String password, String date, boolean is_login, boolean isNotRemoved, List<ShopAdministrator> administrators, Map<Shop, Basket> userBaskets) {
        super(username, paymentMethod, password, date, is_login, isNotRemoved, administrators, userBaskets);
    }

    public SystemManager(String password, String date, boolean is_login, boolean isNotRemoved, List<ShopAdministrator> administrators, Map<Shop, Basket> userBaskets) {
        super(password, date, is_login, isNotRemoved, administrators, userBaskets);
    }

    public SystemManager(String username, PaymentMethod paymentMethod, Map<Integer, BidOffer> shoppingBids, String password, String date, boolean is_login, boolean isNotRemoved, List<ShopAdministrator> administrators, Map<Shop, Basket> userBaskets) {
        super(username, paymentMethod, shoppingBids, password, date, is_login, isNotRemoved, administrators, userBaskets);
    }

    public SystemManager(String username, String password, String date, boolean is_login, boolean isNotRemoved, PaymentMethod paymentMethod, List<ShopAdministrator> administrators){
        super(username, password, date, is_login, isNotRemoved, paymentMethod, administrators);
    }

    public SystemManager(String username, String password, String date, boolean is_login, boolean isNotRemoved, PaymentMethod paymentMethod, List<ShopAdministrator> administrators, Map<Shop, Basket> userBaskets) {
        super(username, password, date, is_login, isNotRemoved, paymentMethod, administrators, userBaskets);
    }
}
