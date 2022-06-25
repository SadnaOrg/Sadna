package ORM.Users;


import ORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
@Entity
@Table(name = "ShopOwner")
public class ShopOwner extends ShopAdministrator implements Serializable {
    boolean isFounder;

    public ShopOwner(List<BaseActionType> action, SubscribedUser user, Shop shop, boolean isFounder) {
        super(action, user, shop);
        this.isFounder = isFounder;
    }

    public ShopOwner(List<BaseActionType> action, List<ShopAdministrator> appoints, boolean isFounder) {
        super(action, appoints);
        this.isFounder = isFounder;
    }

    public ShopOwner(List<BaseActionType> action, SubscribedUser user, List<ShopAdministrator> appoints, boolean isFounder) {
        super(action, user, appoints);
        this.isFounder = isFounder;
    }

    public ShopOwner() {
        super();
    }

    public boolean isFounder() {
        return isFounder;
    }
}
