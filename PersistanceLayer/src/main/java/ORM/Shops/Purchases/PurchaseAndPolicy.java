package ORM.Shops.Purchases;

import ORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;

@Entity
@Table(name = "PurchaseAndPolicy")
public class PurchaseAndPolicy extends PurchasePolicy{
    @OneToMany
    private Collection<PurchasePolicy> purchasePolicies;

    public PurchaseAndPolicy() {
    }

    public PurchaseAndPolicy(Collection<PurchasePolicy> purchasePolicies) {
        this.purchasePolicies = purchasePolicies;
    }

    public PurchaseAndPolicy(Shop shop, int ID, Collection<PurchasePolicy> purchasePolicies) {
        super(shop, ID);
        this.purchasePolicies = purchasePolicies;
    }

    public Collection<PurchasePolicy> getPurchasePolicies() {
        return purchasePolicies;
    }
}