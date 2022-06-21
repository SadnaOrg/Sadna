package BusinessLayer.Mappers;

import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Mappers.UserMappers.SubscribedUserMapper;

public class MapperController {
    private ShopMapper shopMapper = ShopMapper.getInstance();

    private SubscribedUserMapper subscribedUserMapper = SubscribedUserMapper.getInstance();
    public ShopMapper getShopMapper() {
        return shopMapper;
    }

    public SubscribedUserMapper getSubscribedUserMapper() {
        return subscribedUserMapper;
    }
}
