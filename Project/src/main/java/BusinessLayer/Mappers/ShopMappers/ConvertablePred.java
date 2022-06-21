package BusinessLayer.Mappers.ShopMappers;

import com.SadnaORM.ShopImpl.ShopObjects.Discounts.DiscountPredDTO;

public interface ConvertablePred {
    DiscountPredDTO conversion(Converter c);
}
