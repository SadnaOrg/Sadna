package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.ShopOwner;
import BusinessLayer.Users.SubscribedUser;
import com.SadnaORM.ShopImpl.ShopObjects.ShopDTO;
import com.SadnaORM.UserImpl.UserObjects.ShopAdministratorDTO;
import com.SadnaORM.UserImpl.UserObjects.ShopOwnerDTO;
import com.SadnaORM.UserImpl.UserObjects.SubscribedUserDTO;
import com.SadnaORM.Users.ShopAdministrator;

import java.util.ArrayList;
import java.util.List;

public class ShopOwnerMapper {
    static private class ShopOwnerMapperHolder {
        static final ShopOwnerMapper mapper = new ShopOwnerMapper();
    }

    public static ShopOwnerMapper getInstance(){
        return ShopOwnerMapperHolder.mapper;
    }

    private ShopOwnerMapper() {

    }

    public ShopOwnerDTO toEntity(ShopOwner entity, Shop shop) {
        List<ShopAdministratorDTO.BaseActionType> action = new ArrayList<>();
        return new ShopOwnerDTO(action, entity.getUserName(), shop.getId(), 0, entity.isFounder());
    }

    public ShopOwner FromEntity(ShopOwnerDTO entity) {
        return null;
    }
}
