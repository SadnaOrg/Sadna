//package ORM.Users;
//
//import ORM.Shops.Shop;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
//@Entity
//@Table(name = "BidApprovals")
//@IdClass(BidApprovalStatus.BidApprovalPK.class)
//public class BidApprovalStatus {
//    @Id
//    @ManyToOne(cascade = CascadeType.ALL)
//    private SubscribedUser user;
//    @Id
//    @ManyToOne(cascade = CascadeType.ALL)
//    private Shop shop;
//    private boolean status;
//
//    public BidApprovalStatus(SubscribedUser user, Shop shop, boolean status) {
//        this.user = user;
//        this.shop = shop;
//        this.status = status;
//    }
//
//    public SubscribedUser getUser() {
//        return user;
//    }
//
//    public void setUser(SubscribedUser user) {
//        this.user = user;
//    }
//
//    public Shop getShop() {
//        return shop;
//    }
//
//    public void setShop(Shop shop) {
//        this.shop = shop;
//    }
//
//    public boolean isStatus() {
//        return status;
//    }
//
//    public void setStatus(boolean status) {
//        this.status = status;
//    }
//
//    public BidApprovalStatus() {
//    }
//
//    public static class BidApprovalPK implements Serializable{
//        private Shop shop;
//        private SubscribedUser user;
//
//        public BidApprovalPK(Shop shop, SubscribedUser user) {
//            this.shop = shop;
//            this.user = user;
//        }
//
//        public BidApprovalPK() {
//        }
//    }
//}
