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
    public ORM.Users.ShopOwner toEntity(ShopOwner entity, SubscribedUser user) {
        return new ORM.Users.ShopOwner(
                entity.getActionsTypes().stream().map(action -> ShopAdministrator.BaseActionType.values()[action.ordinal()])
                        .collect(Collectors.toList()),
                user, shopMapper.run().toEntity(shopMapper.run().findById(entity.getShopID())), entity.isFounder());
    }

    public ShopOwner fromEntity(ORM.Users.ShopOwner entity) {
        BusinessLayer.Shops.Shop s = shopMapper.run().fromEntityNoAdmin(entity.getUser(),entity.getShop()); // to avoid infinite recursion
        BusinessLayer.Users.SubscribedUser u = subscribedUserMapper.fromEntityNoAdmin(entity.getUser(),entity.getShop()); // to avoid infinite recursion...
        BusinessLayer.Users.ShopAdministrator appointer = shopAdministratorMapper.fromEntity(entity.getAppointer()); // recursive but finite, no circles
        Collection<BaseActionType> actions = entity.getAction().stream().map(baseActionType -> BusinessLayer.Users.BaseActions.BaseActionType.values()[baseActionType.ordinal()]).toList();
        Map<BaseActionType, BaseAction> adminActions = new ConcurrentHashMap<>();
        for(BaseActionType actionType: actions){
            adminActions.put(actionType,BaseActionType.getAction(u,s, actionType)); // change the user later
        }
        ConcurrentLinkedDeque<BusinessLayer.Users.ShopAdministrator> appoints = new ConcurrentLinkedDeque<>();
        List<ORM.Users.ShopAdministrator> administratorAppoints = entity.getAppoints();
        for (ORM.Users.ShopAdministrator admin:
             administratorAppoints) {
            appoints.add(shopAdministratorMapper.fromEntity(admin)); //recursive but finite,no circles
        }
        ShopOwner owner = new ShopOwner(s, u,appointer.getSubscribed().getUserName(),entity.isFounder(), adminActions);
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
