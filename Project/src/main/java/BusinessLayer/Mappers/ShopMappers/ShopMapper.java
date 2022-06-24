package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Shops.Shop;
import ORM.DAOs.DBImpl;
import ORM.DAOs.Shops.ShopDAO;
import ORM.Users.SubscribedUser;

import java.util.*;

public class ShopMapper implements DBImpl<Shop, Integer>, CastEntity<ORM.Shops.Shop, Shop> {

    private final ShopDAO dao = new ShopDAO();

    // return the shop without a singe admin with the name of user.
    public Shop fromEntityNoAdmin(SubscribedUser user, ORM.Shops.Shop shop) {
        return null;
    }

    public ORM.Shops.Shop toEntityNoAdmin(Shop shop,SubscribedUser user) {
        return null;
    }

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
        return null;
    }

    @Override
    public Shop fromEntity(ORM.Shops.Shop entity) {
        return null;
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
