package ORM.Shops.Discounts;


import ORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;
@Entity
@Table(name = "MaxDiscountPolicy")
public class MaxDiscountPolicy extends DiscountPolicy{
    @OneToMany
    private Collection<DiscountPolicy> discountPolicies;

    public MaxDiscountPolicy() {
    }

    public MaxDiscountPolicy(Shop shop, int ID, Collection<DiscountPolicy> discountPolicies) {
        super(shop, ID);
        this.discountPolicies = discountPolicies;
    }

    public MaxDiscountPolicy(Collection<DiscountPolicy> discountPolicies) {
        this.discountPolicies = discountPolicies;
    }

    public Collection<DiscountPolicy> getDiscountPolicies() {
        return discountPolicies;
    }
}
