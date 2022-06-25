package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import BusinessLayer.Mappers.UserMappers.ShopAdministratorMapper;
import BusinessLayer.Mappers.UserMappers.ShopOwnerMapper;
import BusinessLayer.Mappers.UserMappers.SubscribedUserMapper;
import BusinessLayer.Shops.Shop;
import ORM.DAOs.DBImpl;
import ORM.DAOs.Shops.ShopDAO;
import ORM.Users.ShopAdministrator;
import ORM.Users.ShopOwner;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ShopMapper implements DBImpl<Shop, Integer>, CastEntity<ORM.Shops.Shop, Shop> {

    private Func<SubscribedUserMapper> subscribedUserMapper = () -> SubscribedUserMapper.getInstance();
    private Func<ShopOwnerMapper> shopOwnerMapper = () -> ShopOwnerMapper.getInstance();
    private final ShopDAO dao = new ShopDAO();

    static private class ShopMapperHolder {
        static final ShopMapper mapper = new ShopMapper();
    }

    public static ShopMapper getInstance(){
        return ShopMapperHolder.mapper;
    }

    private ShopMapper() {

    }

    @Override
    public ORM.Shops.Shop toEntity(Shop entity) {
        ORM.Shops.Shop shop = new ORM.Shops.Shop(entity.getId(), entity.getName(), entity.getDescription(),
                subscribedUserMapper.run().toEntity(entity.getFounder().getUser()), true,
                ORM.Shops.Shop.State.values()[entity.getState().ordinal()], new ArrayList<>(), new ConcurrentHashMap<>(),
                new ConcurrentHashMap<>(), new ConcurrentHashMap<>());

        //shop.getShopAdministrators().put(shop.getFounder().getUser(), shop.getFounder());

        //for (ShopAdministrator admin : shop.getFounder().getUser().getAdministrators()){
        //    if (admin.getUser().getUsername() == shop.getFounder().getUser().getUsername()) {
        //        admin.setUser(shop.getFounder().getUser());
        //    }
        //    admin.setShop(shop);
        //};
        return shop;
    }

    @Override
    public Shop fromEntity(ORM.Shops.Shop entity) {
        return new Shop(entity.getId(), entity.getName(), entity.getDescription(),
                Shop.State.values()[entity.getState().ordinal()], shopOwnerMapper.run().fromEntity(entity.getFounder()),
                null, null, null, null);
    }

    @Override
    public void save(Shop entity) {
        dao.save(toEntity(entity));
    }

    @Override
    public void update(Shop entity) {
        dao.update(toEntity(entity));
    }

    @Override
    public void delete(Integer key) {
        dao.delete(key);
    }

    @Override
    public Shop findById(Integer key) {
        return fromEntity(dao.findById(key));
    }

    @Override
    public Collection<Shop> findAll() {
        return dao.findAll().stream().map(this::fromEntity).toList();
    }
}
