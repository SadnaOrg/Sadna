package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import BusinessLayer.Users.ShopAdministrator;
import BusinessLayer.Users.ShopManager;
import BusinessLayer.Users.ShopOwner;
import ORM.Users.SubscribedUser;
import org.mockito.internal.matchers.InstanceOf;


public class ShopAdministratorMapper {
    private Func<ShopOwnerMapper> shopOwnerMapper = () -> ShopOwnerMapper.getInstance();
    private Func<ShopManagerMapper> shopManagerMapper = () -> ShopManagerMapper.getInstance();
    static private class ShopAdministratorMapperHolder {
        static final ShopAdministratorMapper mapper = new ShopAdministratorMapper();
    }

    public static ShopAdministratorMapper getInstance(){
        return ShopAdministratorMapper.ShopAdministratorMapperHolder.mapper;
    }

    private ShopAdministratorMapper() {

    }

    public ORM.Users.ShopAdministrator toEntity(ShopAdministrator entity) {
        return entity instanceof ShopOwner ? shopOwnerMapper.run().toEntity((ShopOwner) entity) : shopManagerMapper.run().toEntity((ShopManager) entity);
    }

    public ShopAdministrator fromEntity(ORM.Users.ShopAdministrator entity) {
        return null;
    }
}
