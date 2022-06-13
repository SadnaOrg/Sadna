package ServiceLayer.Objects.Policies.Discount;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.SubscribedUser;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum DiscountPolicyType {
    AND(1),
    OR(2),
    XOR(3),
    PLUS(4),
    MAX(5),
    PRODUCT_QUANTITY(6),
    PRODUCT(7),
    QUANTITY_IN_PRICE(8),
    CATEGORY(9),
    SHOP_DISCOUNT(10),;

    private final int code;

    DiscountPolicyType(int i) {
        this.code = i;
    }

    private static final Map<Integer,DiscountPolicyType> lookup
            = new HashMap<>();

    static {
        for(DiscountPolicyType s : EnumSet.allOf(DiscountPolicyType.class))
            lookup.put(s.getCode(), s);
    }

    public int getCode() {
        return this.code;
    }

    public static DiscountPolicyType lookup(int code){
        return lookup.get(code);
    }
}