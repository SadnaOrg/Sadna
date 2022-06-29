package ORM.Shops;

import ORM.Users.SubscribedUser;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "purchaseHistory")
@IdClass(PurchaseHistory.PurchaseHistoryPKID.class)
public class PurchaseHistory {
    public PurchaseHistory(Shop shop, SubscribedUser user) {
        this.shop = shop;
        this.user = user;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "shopID")
    private Shop shop;

    @Id
    @ManyToOne
    @JoinColumn(name = "username")
    private SubscribedUser user;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Collection<Purchase> past_purchases;

    public PurchaseHistory() {

    }

    public Collection<Purchase> getPast_purchases() {
        return past_purchases;
    }

    public void setPast_purchases(Collection<Purchase> past_purchases) {
        this.past_purchases = past_purchases;
    }

    public static class PurchaseHistoryPKID implements Serializable {
        private Shop shop;
        private SubscribedUser user;

        public PurchaseHistoryPKID() {

        }
    }
}
