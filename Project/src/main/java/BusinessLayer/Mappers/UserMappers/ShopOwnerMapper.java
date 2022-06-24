package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.Func;
import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Users.BaseActions.BaseAction;
import BusinessLayer.Users.BaseActions.BaseActionType;
import BusinessLayer.Users.ShopOwner;
import ORM.Shops.Shop;
import ORM.Users.ShopAdministrator;
import ORM.Users.SubscribedUser;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class ShopOwnerMapper {
    private Func<ShopMapper> shopMapper = () -> ShopMapper.getInstance();
    private SubscribedUserMapper subscribedUserMapper = SubscribedUserMapper.getInstance();
    private ShopAdministratorMapper shopAdministratorMapper = ShopAdministratorMapper.getInstance();

    public ORM.Users.ShopOwner toEntity(ShopOwner entity) {
        ORM.Users.SubscribedUser user = subscribedUserMapper.toEntityNoAdmin(entity.getUser(),entity.getShop());
        List<ORM.Users.ShopAdministrator.BaseActionType> actionTypes = entity.getActionsTypes().stream().map(actionType -> ShopAdministrator.BaseActionType.values()[actionType.ordinal()]).toList();
        ORM.Shops.Shop shop = shopMapper.run().toEntityNoAdmin(entity.getShop(), user);
        List<ORM.Users.ShopAdministrator> appoints = entity.getAppoints().stream().map(admin -> shopAdministratorMapper.toEntity(admin)).toList();
        ORM.Users.SubscribedUser appointer = subscribedUserMapper.toEntity(subscribedUserMapper.findById(entity.getAppointer()));
        ORM.Users.ShopAdministrator myAppointer = appointer.getAdministrators().get(shop.getId());
        ORM.Users.ShopOwner owner = new ORM.Users.ShopOwner(actionTypes,  user,  shop,  appoints, entity.isFounder(), myAppointer);
        user.addAdministrator(owner);
        shop.getShopAdministrators().put(user,owner);
        return owner;
    }

    public ShopOwner fromEntity(ORM.Users.ShopOwner entity) {
        BusinessLayer.Shops.Shop s = shopMapper.run().fromEntityNoAdmin(entity.getUser(),entity.getShop());
        BusinessLayer.Users.SubscribedUser u = subscribedUserMapper.fromEntityNoAdmin(entity.getUser(),entity.getShop());
        BusinessLayer.Users.ShopAdministrator appointer = shopAdministratorMapper.fromEntity(entity.getAppointer());
        Collection<BaseActionType> actions = entity.getAction().stream().map(baseActionType -> BusinessLayer.Users.BaseActions.BaseActionType.values()[baseActionType.ordinal()]).toList();
        Map<BaseActionType, BaseAction> adminActions = new ConcurrentHashMap<>();
        for(BaseActionType actionType: actions){
            adminActions.put(actionType,BaseActionType.getAction(u,s, actionType));
        }
        ConcurrentLinkedDeque<BusinessLayer.Users.ShopAdministrator> appoints = new ConcurrentLinkedDeque<>();
        List<ORM.Users.ShopAdministrator> administratorAppoints = entity.getAppoints();
        for (ORM.Users.ShopAdministrator admin:
             administratorAppoints) {
            appoints.add(shopAdministratorMapper.fromEntity(admin));
        }
        ShopOwner owner = new ShopOwner(s, u,appointer.getSubscribed().getUserName(),entity.isFounder(), adminActions, appoints);
        u.addAdministrator(s.getId(),owner);
        s.addAdministrator(u.getUserName(),owner);
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
}
