package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Users.ShopManager;

public class ShopManagerMapper implements CastEntity<ORM.Users.ShopManager, ShopManager> {
    @Override
    public ORM.Users.ShopManager toEntity(ShopManager entity) {
        return null;
    }

    @Override
    public ShopManager fromEntity(ORM.Users.ShopManager entity) {
        return null;
    }

    static private class ShopManagerMapperHolder {
        static final ShopManagerMapper mapper = new ShopManagerMapper();
    }

    public static ShopManagerMapper getInstance(){
        return ShopManagerMapperHolder.mapper;
    }

    private ShopManagerMapper() {

    }
}