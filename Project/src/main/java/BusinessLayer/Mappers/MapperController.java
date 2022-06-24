package BusinessLayer.Mappers;

import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Mappers.UserMappers.SubscribedUserMapper;

public class MapperController {

    static private class MapperControllerHolder {
        static final MapperController mapper = new MapperController();
    }

    public static MapperController getInstance(){
        return MapperControllerHolder.mapper;
    }

    public ShopMapper getShopMapper() {
        return shopMapper;
    }

    public SubscribedUserMapper getSubscribedUserMapper() {
        return subscribedUserMapper;
    }

    private ShopMapper shopMapper = ShopMapper.getInstance();
    private SubscribedUserMapper subscribedUserMapper = SubscribedUserMapper.getInstance();
}
