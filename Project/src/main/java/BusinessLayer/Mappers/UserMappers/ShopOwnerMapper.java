package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Users.ShopOwner;
import ORM.Users.ShopAdministrator;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ShopOwnerMapper implements CastEntity<ORM.Users.ShopOwner, ShopOwner> {
    @Override
    public ORM.Users.ShopOwner toEntity(ShopOwner entity) {
        return new ORM.Users.ShopOwner(
                entity.getActionsTypes().stream().map(action -> ShopAdministrator.BaseActionType.values()[action.ordinal()])
                        .collect(Collectors.toList()), null, entity.isFounder());
    }

    @Override
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
