package ORM.Shops;

import ORM.Users.SubscribedUser;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "purchase")
public class Purchase {
    @Id
    private int transactionid;

    @OneToMany(
            mappedBy = "purchase",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Collection<ProductInfo> productInfos;
    private String dateOfPurchase;
    @ManyToOne
    @JoinColumn(name = "shopID")
    private Shop shop;

    public Purchase(int transactionid, Collection<ProductInfo> productInfos, String dateOfPurchase, Shop shop, SubscribedUser user) {
        this.transactionid = transactionid;
        this.productInfos = productInfos;
        this.dateOfPurchase = dateOfPurchase;
        this.shop = shop;
        this.user = user;
    }

    public Purchase(int transactionid, Collection<ProductInfo> productInfos, String dateOfPurchase) {
        this.transactionid = transactionid;
        this.productInfos = productInfos;
        this.dateOfPurchase = dateOfPurchase;
    }

    @ManyToOne
    @JoinColumn(name = "username")
    private SubscribedUser user;

    public Purchase() {
    }

    public Collection<ProductInfo> getProductInfos() {
        return productInfos;
    }

    public void setProductInfos(Collection<ProductInfo> productInfos) {
        this.productInfos = productInfos;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
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

    public int getTransactionid() {
        return transactionid;
    }
}
