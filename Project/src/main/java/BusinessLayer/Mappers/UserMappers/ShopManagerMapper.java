package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Users.ShopManager;
import ORM.Users.ShopAdministrator;
import ORM.Users.SubscribedUser;

import java.util.List;
import java.util.stream.Collectors;

public class ShopManagerMapper {
    private Func<ShopMapper> shopMapper = () -> ShopMapper.getInstance();
    private SubscribedUserMapper subscribedUserMapper = SubscribedUserMapper.getInstance();
    private ShopAdministratorMapper shopAdministratorMapper = ShopAdministratorMapper.getInstance();

    public ORM.Users.ShopManager toEntity(ShopManager entity, SubscribedUser user) {
        List<ORM.Users.ShopAdministrator.BaseActionType> actionTypes = entity.getActionsTypes().stream().map(actionType -> ShopAdministrator.BaseActionType.values()[actionType.ordinal()]).toList();
        ORM.Shops.Shop shop = shopMapper.run().toEntityNoAdmin(entity.getShop(), user);
        List<ORM.Users.ShopAdministrator> appoints = entity.getAppoints().stream().map(admin -> shopAdministratorMapper.toEntity(admin, subscribedUserMapper.toEntity(admin.getUser()))).toList();
        ORM.Users.ShopAdministrator appointer = null;
        return null;
    }

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