package ORM.Users;


import ORM.Shops.Shop;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@IdClass(ShopAdministrator.ShopAdministratorPK.class)
public abstract class ShopAdministrator implements Serializable{
    @ElementCollection
    @CollectionTable(
            name = "AdministratorPermissions",
            joinColumns = {
                    @JoinColumn(name="ADMIN_NAME", referencedColumnName="user_username"),
                    @JoinColumn(name="SHOP_ID", referencedColumnName="shop_id")
            }
    )
    private List<BaseActionType> action;
    @Id
    @ManyToOne
    private Shop shop;
    @Id
    @ManyToOne
    private SubscribedUser user;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name="MyAppointer", referencedColumnName="user_username"),
            @JoinColumn(name="SHOP_ID", referencedColumnName="shop_id")
    })
    private List<ShopAdministrator> appoints;

    public ShopAdministrator(List<BaseActionType> action, SubscribedUser user,Shop shop, List<ShopAdministrator> appoints) {
        this.action = action;
        this.user = user;
        this.shop = shop;
        this.appoints = appoints;
    }

    public ShopAdministrator(List<BaseActionType> action, SubscribedUser user,Shop shop) {
        this.action = action;
        this.user = user;
        this.shop = shop;
        this.appoints = new ArrayList<>();
    }

    public ShopAdministrator(){

    }

    public List<BaseActionType> getAction() {
        return action;
    }

    public void setAction(List<BaseActionType> action) {
        this.action = action;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public SubscribedUser getUser() {
        return user;
    }

    public void setUser(SubscribedUser user) {
        this.user = user;
    }

    public List<ShopAdministrator> getAppoints() {
        return appoints;
    }

    public void setAppoints(List<ShopAdministrator> appoints) {
        this.appoints = appoints;
    }

    public static class ShopAdministratorPK implements Serializable {
        private Shop shop;
        private SubscribedUser user;

        public ShopAdministratorPK(SubscribedUser user, Shop shop) {
            this.user = user;
            this.shop = shop;
        }

        public ShopAdministratorPK(){

        }
    }

    public enum BaseActionType {
        STOCK_MANAGEMENT(1),
        SET_PURCHASE_POLICY(2),
        ASSIGN_SHOP_OWNER(4),
        ASSIGN_SHOP_MANAGER(6),
        CHANGE_MANAGER_PERMISSION(7),
        CLOSE_SHOP(9),
        REOPEN_SHOP(10),
        ROLE_INFO(11),
        HISTORY_INFO(13),
        REMOVE_ADMIN(5),
        ;

        private final int code;

        BaseActionType(int i) {
            this.code = i;
        }
    }
}

