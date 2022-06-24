package ORM.Shops.Purchases;

import ORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;

@Entity
@Table(name = "PurchaseOrPolicy")
public class PurchaseOrPolicy extends PurchasePolicy{
    @OneToMany
    private Collection<PurchasePolicy> purchasePolicies;

    public PurchaseOrPolicy() {
    }

    public PurchaseOrPolicy(Collection<PurchasePolicy> purchasePolicies) {
        this.purchasePolicies = purchasePolicies;
    }

    public PurchaseOrPolicy(Shop shop, int ID, Collection<PurchasePolicy> purchasePolicies) {
        super(shop, ID);
        this.purchasePolicies = purchasePolicies;
    }

    public Collection<PurchasePolicy> getPurchasePolicies() {
        return purchasePolicies;
    }
}
