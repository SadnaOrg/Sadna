package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Users.ShopManager;
import BusinessLayer.Users.ShopManager;
import ORM.DAOs.Users.ShopManagerDAO;
import ORM.Users.ShopAdministrator;
import ORM.Users.SubscribedUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class ShopManagerMapper implements CastEntity<ORM.Users.ShopManager, ShopManager>{
    private ShopManagerDAO dao = new ShopManagerDAO();
    private Func<ShopAdministratorMapper> shopAdministratorMapper = () -> ShopAdministratorMapper.getInstance();
    private Func<SubscribedUserMapper> subscribedUserMapper = () -> SubscribedUserMapper.getInstance();
    private Func<ShopMapper> shopMapper = () -> ShopMapper.getInstance();
    static private class ShopManagerMapperHolder {

        static final ShopManagerMapper mapper = new ShopManagerMapper();
    }
    public static ShopManagerMapper getInstance(){
        return ShopManagerMapperHolder.mapper;
    }

    private ShopManagerMapper() {

    }

    @Override
    public ORM.Users.ShopManager toEntity(ShopManager entity) {
        ORM.Users.ShopManager ormManager = findORMById(entity.getUserName(), entity.getShopID());
        if (ormManager == null) {
            ORM.Users.ShopManager manager = new ORM.Users.ShopManager(
                    entity.getActionsTypes().stream().map(action -> ORM.Users.ShopAdministrator.BaseActionType.values()[action.ordinal()])
                            .collect(Collectors.toList()), subscribedUserMapper.run().toEntity(entity.getSubscribed()),
                    shopMapper.run().findORMById(entity.getShopID()), new ArrayList<>());
            manager.setAppointer(entity.getAppointer());
            List<ShopAdministrator> appoints = entity.getAppoints().stream().map(admin ->
                    admin.getUserName() == entity.getUserName() ?
                            manager : shopAdministratorMapper.run().toEntity(admin)).toList();
            manager.setAppoints(appoints);
            return manager;
        }
        else {
            ormManager.getAction().clear();
            ormManager.getAction().addAll(entity.getActionsTypes().stream().map(action -> ORM.Users.ShopAdministrator.BaseActionType.values()[action.ordinal()]).toList());
            ormManager.getAppoints().clear();
            ormManager.getAppoints().addAll(entity.getAppoints().stream().map(admin -> shopAdministratorMapper.run().toEntity(admin)).toList());
            ormManager.setAppointer(entity.getAppointer());
            return ormManager;
        }
    }

    @Override
    public ShopManager fromEntity(ORM.Users.ShopManager entity) {
        ShopManager manager = new ShopManager(shopMapper.run().fromEntity(entity.getShop()), subscribedUserMapper.run().fromEntity(entity.getUser()), entity.getAppointer());
        ConcurrentLinkedDeque<BusinessLayer.Users.ShopAdministrator> appoints = entity.getAppoints().stream().map(admin ->
                admin.getUser().getUsername() == entity.getUser().getUsername() ?
                        manager : shopAdministratorMapper.run().fromEntity(admin)).collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
        manager.setAppoints(appoints);
        return manager;
    }

    private ORM.Users.ShopManager findORMById(String username, int shopID) {
        return dao.findById(new ORM.Users.ShopAdministrator.ShopAdministratorPK(subscribedUserMapper.run().findORMById(username),
                shopMapper.run().findORMById(shopID)));
    }
}