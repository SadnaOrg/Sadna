package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import BusinessLayer.Users.ShopOwner;
import BusinessLayer.Users.SubscribedUser;
import ORM.DAOs.Users.ShopOwnerDAO;
import ORM.Users.ShopAdministrator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class ShopOwnerMapper implements CastEntity<ORM.Users.ShopOwner, ShopOwner> {
    private Func<ShopAdministratorMapper> shopAdministratorMapper = () -> ShopAdministratorMapper.getInstance();
    private Func<SubscribedUserMapper> subscribedUserMapper = () -> SubscribedUserMapper.getInstance();
    @Override
    public ORM.Users.ShopOwner toEntity(ShopOwner entity) {
        ORM.Users.ShopOwner owner = new ORM.Users.ShopOwner(
                entity.getActionsTypes().stream().map(action -> ShopAdministrator.BaseActionType.values()[action.ordinal()])
                        .collect(Collectors.toList()), null, entity.isFounder());

        List<ShopAdministrator> appoints = entity.getAppoints().stream().map(admin ->
                admin.getUserName() == entity.getUserName() ?
                owner : shopAdministratorMapper.run().toEntity(admin)).toList();
        owner.setAppoints(appoints);
        return owner;
    }

    @Override
    public ShopOwner fromEntity(ORM.Users.ShopOwner entity) {
        return new ShopOwner(entity.getUser().getUsername(), subscribedUserMapper.run().fromEntity(entity.getUser()),
                entity.isFounder(), new ConcurrentLinkedDeque<>());
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
