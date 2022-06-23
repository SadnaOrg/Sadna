package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Users.Basket;
public class BasketMapper {

    static private class BasketMapperHolder {
        static final BasketMapper mapper = new BasketMapper();
    }

    public static BasketMapper getInstance(){
        return BasketMapperHolder.mapper;
    }

    private BasketMapper() {

    }
}
