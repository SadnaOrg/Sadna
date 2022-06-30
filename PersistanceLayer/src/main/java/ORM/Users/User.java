package ORM.Users;

import javax.persistence.*;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User {
    @Id
    protected String username;
    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    protected PaymentMethod paymentMethod;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "userBids",
    joinColumns = {
            @JoinColumn(name = "username", referencedColumnName = "username")
    },
    inverseJoinColumns = {
            @JoinColumn(name = "shopID", referencedColumnName = "shop_id"),
            @JoinColumn(name = "usernameBidder",referencedColumnName = "user_username")
    })
    @MapKey(name = "shop")
    protected Map<Integer, BidOffer> shoppingBids;

    public User(String username, PaymentMethod paymentMethod, Map<Integer, BidOffer> shoppingBids) {
        this.username = username;
        this.paymentMethod = paymentMethod;
        this.shoppingBids = shoppingBids;
    }

    public User(){

    }

    public User(String username, PaymentMethod paymentMethod) {
        this.username = username;
        this.paymentMethod = paymentMethod;
        //this.shoppingBids = shoppingBids;
    }

//    public Map<Integer, BidOffer> getShoppingBids() {
//        return shoppingBids;
//    }

    public String getUsername() {
        return username;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
