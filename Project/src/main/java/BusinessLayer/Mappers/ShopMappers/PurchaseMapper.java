package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Shops.Purchase;

public class PurchaseMapper  implements CastEntity <ORM.Shops.Purchase, Purchase>{

    static private class PurchaseMapperHolder {
        static final PurchaseMapper mapper = new PurchaseMapper();

    }

    public static PurchaseMapper getInstance(){
        return PurchaseMapper.PurchaseMapperHolder.mapper;
    }

    private PurchaseMapper() {

    }

    @Override
    public ORM.Shops.Purchase toEntity(Purchase entity) {
        return null;
    }

    @Override
    public Purchase fromEntity(ORM.Shops.Purchase entity) {
        return null;
    }
}
