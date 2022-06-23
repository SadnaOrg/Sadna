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
}
