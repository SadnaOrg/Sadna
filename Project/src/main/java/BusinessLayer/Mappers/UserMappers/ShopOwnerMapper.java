package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.Func;
import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Users.ShopOwner;
import ORM.Shops.Shop;
import ORM.Users.ShopAdministrator;
import ORM.Users.SubscribedUser;

import java.util.stream.Collectors;

public class ShopOwnerMapper {
    private Func<ShopMapper> shopMapper = () -> ShopMapper.getInstance();
    public ORM.Users.ShopOwner toEntity(ShopOwner entity, SubscribedUser user) {
        return new ORM.Users.ShopOwner(
                entity.getActionsTypes().stream().map(action -> ShopAdministrator.BaseActionType.values()[action.ordinal()])
                        .collect(Collectors.toList()),
                user, shopMapper.run().toEntity(shopMapper.run().findById(entity.getShopID())), entity.isFounder());
    }

    public ShopOwner fromEntity(ORM.Users.ShopOwner entity) {
        return null;
    }

    static private class ShopOwnerMapperHolder {
        static final ShopOwnerMapper mapper = new ShopOwnerMapper();
    }

    public static ShopOwnerMapper getInstance(){
        return ShopOwnerMapperHolder.mapper;
    }

    private ShopOwnerMapper() {

    }
}
