package ORM.Shops.Discounts;


import ORM.Shops.Shop;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@IdClass(DiscountPolicy.DiscountPolicyPK.class)
public abstract class DiscountPolicy {
    @Id
    @ManyToOne
    protected Shop shop;

    @Id
    protected int ID;

    public DiscountPolicy(Shop shop, int ID) {
        this.shop = shop;
        this.ID = ID;
    }

    public DiscountPolicy() {
    }

    public Shop getShop() {
        return shop;
    }

    public int getID() {
        return ID;
    }

    public static class DiscountPolicyPK implements Serializable {
        private Shop shop;
        private int ID;

        public DiscountPolicyPK(Shop shop, int ID) {
            this.shop = shop;
            this.ID = ID;
        }

        public DiscountPolicyPK() {
        }
    }
}