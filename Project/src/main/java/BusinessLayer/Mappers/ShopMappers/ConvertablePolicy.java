package BusinessLayer.Mappers.ShopMappers;

import com.SadnaORM.ShopImpl.ShopObjects.Policies.PurchasePolicyDTO;

public interface ConvertablePolicy {
    PurchasePolicyDTO conversion(Converter c);
}
