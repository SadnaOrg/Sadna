package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Users.Basket;

public class PurchaseHistoryMapper {
    static private class PurchaseHistoryMapperHolder {
        static final PurchaseHistoryMapper mapper = new PurchaseHistoryMapper();
    }

    public static PurchaseHistoryMapper getInstance(){
        return PurchaseHistoryMapperHolder.mapper;
    }

    private PurchaseHistoryMapper() {

    }

    public com.SadnaORM.Users.Basket toEntity(Basket entity, String subscribedUser) {
        return null;
    }

    public Basket FromEntity(com.SadnaORM.Users.Basket entity) {
        return null;
    }
}
