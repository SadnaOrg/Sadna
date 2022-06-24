package ORM.Shops.Discounts;

import com.SadnaORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;

@Entity
@Table(name = "DiscountPlusPolicy")
public class DiscountPlusPolicy extends DiscountPolicy{
    @OneToMany
    private Collection<DiscountPolicy> discountPolicies;

    public DiscountPlusPolicy() {
    }

    public DiscountPlusPolicy(Shop shop, int ID, Collection<DiscountPolicy> discountPolicies) {
        super(shop, ID);
        this.discountPolicies = discountPolicies;
    }

    public DiscountPlusPolicy(Collection<DiscountPolicy> discountPolicies) {
        this.discountPolicies = discountPolicies;
    }

    public Collection<DiscountPolicy> getDiscountPolicies() {
        return discountPolicies;
    }
}
