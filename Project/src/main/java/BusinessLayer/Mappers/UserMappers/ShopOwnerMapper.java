package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Users.BaseActions.BaseActionType;
import BusinessLayer.Users.ShopOwner;
import ORM.DAOs.DBImpl;
import ORM.DAOs.Users.ShopOwnerDAO;
import BusinessLayer.Users.ShopAdministrator;
import ORM.Shops.Shop;
import ORM.Users.SubscribedUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class ShopOwnerMapper implements CastEntity<ORM.Users.ShopOwner, ShopOwner> {
    private ShopOwnerDAO dao = new ShopOwnerDAO();
    private Func<ShopAdministratorMapper> shopAdministratorMapper = () -> ShopAdministratorMapper.getInstance();
    private Func<SubscribedUserMapper> subscribedUserMapper = () -> SubscribedUserMapper.getInstance();
    private Func<ShopMapper> shopMapper = () -> ShopMapper.getInstance();
    @Override
    public ORM.Users.ShopOwner toEntity(ShopOwner entity) {
        ORM.Users.ShopOwner ormOwner = findORMById(entity.getUserName(), entity.getShopID());
        if (ormOwner == null) {
            ORM.Users.ShopOwner owner = new ORM.Users.ShopOwner(
                    entity.getActionsTypes().stream().map(action -> ORM.Users.ShopAdministrator.BaseActionType.values()[action.ordinal()])
                            .collect(Collectors.toList()), null, entity.isFounder());

            List<ORM.Users.ShopAdministrator> appoints = entity.getAppoints().stream().map(admin ->
                    admin.getUserName() == entity.getUserName() ?
                            owner : shopAdministratorMapper.run().toEntity(admin)).toList();
            owner.setAppoints(appoints);
            return owner;
        }
        else {
            return ormOwner;
        }
    }

    @Override
    public ShopOwner fromEntity(ORM.Users.ShopOwner entity) {
        ShopOwner owner = new ShopOwner(entity.getUser().getUsername(), subscribedUserMapper.run().fromEntity(entity.getUser()),
                entity.isFounder(), new ConcurrentLinkedDeque<>());
        ConcurrentLinkedDeque<ShopAdministrator> appoints = entity.getAppoints().stream().map(admin ->
                admin.getUser().getUsername() == entity.getUser().getUsername() ?
                        owner : shopAdministratorMapper.run().fromEntity(admin)).collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
        owner.setAppoints(appoints);
        return owner;
    }

    static private class ShopOwnerMapperHolder {
        static final ShopOwnerMapper mapper = new ShopOwnerMapper();
    }

    public static ShopOwnerMapper getInstance(){
        return ShopOwnerMapperHolder.mapper;
    }

    private ShopOwnerMapper() {

    }

    private ORM.Users.ShopOwner findORMById(String username, int shopID) {
        return dao.findById(new ORM.Users.ShopAdministrator.ShopAdministratorPK(subscribedUserMapper.run().findORMById(username),
                shopMapper.run().findORMById(shopID)));
    }
}
