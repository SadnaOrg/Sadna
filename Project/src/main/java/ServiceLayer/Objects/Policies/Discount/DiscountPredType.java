package ServiceLayer.Objects.Policies.Discount;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum DiscountPredType {
    VALIDATE_BASKET_QUANTITY_DISCOUNT(1),
    VALIDATE_BASKET_VALUE_DISCOUNT(2),
    VALIDATE_PRODUCT_QUANTITY_DISCOUNT(3),;

    private final int code;

    DiscountPredType(int i) {
        this.code = i;
    }

    private static final Map<Integer,DiscountPredType> lookup
            = new HashMap<>();

    static {
        for(DiscountPredType s : EnumSet.allOf(DiscountPredType.class))
            lookup.put(s.getCode(), s);
    }

    public int getCode() {
        return this.code;
    }

    public static DiscountPredType lookup(int code){
        return lookup.get(code);
    }
}
