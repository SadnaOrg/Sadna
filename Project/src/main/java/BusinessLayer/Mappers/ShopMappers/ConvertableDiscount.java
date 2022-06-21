package BusinessLayer.Mappers.ShopMappers;

import com.SadnaORM.ShopImpl.ShopObjects.Discounts.DiscountPolicyDTO;

public interface ConvertableDiscount {
    DiscountPolicyDTO conversion(Converter c);
}
