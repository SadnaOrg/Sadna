package ORM.Users;

import ORM.Shops.Shop;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;
@Entity
@Table(name = "BidOffers")
@IdClass(BidOffer.BidOfferPK.class)
public class BidOffer {
    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    private Shop shop;
    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "bidApprovals",
    joinColumns = {
            @JoinColumn(name = "shopID", referencedColumnName = "shop_id"),
            @JoinColumn(name = "username", referencedColumnName = "user_username")
    },
    inverseJoinColumns = {
            @JoinColumn(name = "productID", referencedColumnName = "product_id"),
            @JoinColumn(name = "shopIDBidApproval",referencedColumnName = "shop_id")
    })
    @MapKey(name = "shop")
    private Map<Integer, ApproveBid> approvals;

    public BidOffer(Shop shop, User user, Map<Integer, ApproveBid> approvals) {
        this.shop = shop;
        this.user = user;
        this.approvals = approvals;
    }

    public BidOffer() {
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<Integer, ApproveBid> getApprovals() {
        return approvals;
    }

    public void setApprovals(Map<Integer, ApproveBid> approvals) {
        this.approvals = approvals;
    }

    public static class BidOfferPK implements Serializable{
        private Shop shop;
        private User user;

        public BidOfferPK(Shop shop, User user) {
            this.shop = shop;
            this.user = user;
        }

        public BidOfferPK() {
        }
    }
}
