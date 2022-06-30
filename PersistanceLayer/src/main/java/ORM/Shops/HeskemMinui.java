package ORM.Shops;

import ORM.Users.AppointApprovalStatus;
import ORM.Users.SubscribedUser;
import com.sun.istack.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "HeskemMinui")
@IdClass(HeskemMinui.HeskemMinuiPK.class)
public class HeskemMinui {
    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    private Shop shop;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    private SubscribedUser adminToAssign;

    @Nullable
    @ManyToOne(cascade = CascadeType.ALL)
    private SubscribedUser appointer;

    @ElementCollection
    @CollectionTable(name = "approvalsHeskem",
    joinColumns = {
            @JoinColumn(name = "shopID" , referencedColumnName = "shop_id"),
            @JoinColumn(name = "Asignee", referencedColumnName = "adminToAssign_username")
    })
    private Collection<AppointApprovalStatus> approvals;

    public HeskemMinui(Shop shop, SubscribedUser adminToAssign, SubscribedUser appointer, Collection<AppointApprovalStatus> approvals) {
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

    public Collection<AppointApprovalStatus> getApprovals() {
        return approvals;
    }

    public void setApprovals(Collection<AppointApprovalStatus> approvals) {
        this.approvals = approvals;
    }

    public static class HeskemMinuiPK implements Serializable{
        private Shop shop;
        private SubscribedUser adminToAssign;

        public HeskemMinuiPK(Shop shop, SubscribedUser adminToAssign) {
            this.shop = shop;
            this.adminToAssign = adminToAssign;
        }

        public HeskemMinuiPK() {
        }
    }
}
