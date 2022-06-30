package ORM.Shops;

import ORM.Users.ShopAdministrator;
import ORM.Users.SubscribedUser;

import javax.persistence.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "HeskemMinui")
public class HeskemMinui {
    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    private Shop shop;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    private SubscribedUser adminToAssign;

    @ManyToOne(cascade = CascadeType.ALL)
    private SubscribedUser appointer;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "HeskemMinuiApprovals",
            joinColumns = {@JoinColumn(name = "Approver", referencedColumnName = "username"),
                           @JoinColumn(name = "shopID", referencedColumnName = "id")})
    @MapKeyJoinColumn(name = "username")
    @Column(name = "status")
    private Map<ShopAdministrator,Boolean> approvals;

    public HeskemMinui(Shop shop, SubscribedUser adminToAssign, SubscribedUser appointer, ConcurrentHashMap<ShopAdministrator, Boolean> approvals) {
        this.shop = shop;
        this.adminToAssign = adminToAssign;
        this.appointer = appointer;
        this.approvals = approvals;
    }

    public HeskemMinui() {
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public SubscribedUser getAdminToAssign() {
        return adminToAssign;
    }

    public void setAdminToAssign(SubscribedUser adminToAssign) {
        this.adminToAssign = adminToAssign;
    }

    public SubscribedUser getAppointer() {
        return appointer;
    }

    public void setAppointer(SubscribedUser appointer) {
        this.appointer = appointer;
    }

    public Map<ShopAdministrator, Boolean> getApprovals() {
        return approvals;
    }

    public void setApprovals(ConcurrentHashMap<ShopAdministrator, Boolean> approvals) {
        this.approvals = approvals;
    }
}
