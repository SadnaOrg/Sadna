package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Users.Basket;
public class BasketMapper implements CastEntity<ORM.Users.Basket, Basket> {

    static private class BasketMapperHolder {

        static final BasketMapper mapper = new BasketMapper();
    }
    public static BasketMapper getInstance(){
        return BasketMapperHolder.mapper;
    }

    private BasketMapper() {

    }

    @Override
    public ORM.Users.Basket toEntity(Basket entity) {
        return null;
    }

    @Override
    public Basket fromEntity(ORM.Users.Basket entity) {
        return null;
    }
}
