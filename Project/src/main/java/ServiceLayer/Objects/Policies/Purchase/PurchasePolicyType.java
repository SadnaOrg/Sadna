package ServiceLayer.Objects.Policies.Purchase;

import ServiceLayer.Objects.Policies.Discount.DiscountPolicyType;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum PurchasePolicyType {
    AND(1),
    OR(2),
    VALIDATE_CATEGORY(3),
    VALIDATE_PRODUCT(4),
    VALIDATE_TIME_STAMP(5),
    VALIDATE_USER_PURCHASE(6),;
    private final int code;

    PurchasePolicyType(int i) {
        this.code = i;
    }

    private static final Map<Integer, DiscountPolicyType> lookup
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
