package ORM.Shops.Discounts;


import ORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Collection;
@Entity
@Table(name = "AndPolicies")
public class AndPolicy extends DiscountPolicy{
    @OneToMany
    private Collection<DiscountPred> discountPreds;

    @OneToOne
    private DiscountPolicy discountPolicy;

    public AndPolicy() {
    }

    public AndPolicy(Shop shop, int ID, Collection<DiscountPred> discountPreds, DiscountPolicy discountPolicy) {
        super(shop, ID);
        this.discountPreds = discountPreds;
        this.discountPolicy = discountPolicy;
    }

    public AndPolicy(Collection<DiscountPred> discountPreds, DiscountPolicy discountPolicy) {
        this.discountPreds = discountPreds;
        this.discountPolicy = discountPolicy;
    }

    public Collection<DiscountPred> getDiscountPreds() {
        return discountPreds;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }
}
