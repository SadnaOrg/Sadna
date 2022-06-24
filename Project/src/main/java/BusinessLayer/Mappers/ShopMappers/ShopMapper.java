package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.UserMappers.ShopAdministratorMapper;
import BusinessLayer.Mappers.UserMappers.ShopOwnerMapper;
import BusinessLayer.Mappers.UserMappers.SubscribedUserMapper;
import BusinessLayer.Shops.Shop;
import ORM.DAOs.DBImpl;
import ORM.DAOs.Shops.ShopDAO;
import ORM.Shops.Discounts.DiscountPlusPolicy;
import ORM.Shops.Product;
import ORM.Shops.PurchaseHistory;
import ORM.Shops.Purchases.PurchaseAndPolicy;
import ORM.Users.Basket;
import ORM.Users.ShopAdministrator;
import ORM.Users.ShopOwner;
import ORM.Users.SubscribedUser;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
        int id = entity.getId();
        String name = entity.getName();
        String description = entity.getDescription();
        ShopOwner founder = ShopOwnerMapper.getInstance().toEntity(entity.getFounder());
        ORM.Shops.Shop.State state = ORM.Shops.Shop.State.OPEN;
        if(!(entity.isOpen()))
            state = ORM.Shops.Shop.State.CLOSED;
        Collection<Product> products = entity.getProducts().values().stream().map(product -> ProductMapper.getInstance().toEntity(product)).toList();
        ConcurrentHashMap<String, BusinessLayer.Users.Basket> usersBaskets = entity.getUsersBaskets();
        Map<SubscribedUser, Basket> DALUsersBaskets = new HashMap<>();
        Map<SubscribedUser, PurchaseHistory> purchaseHistory = null;
        Map<SubscribedUser, ShopAdministrator> shopAdministrators = null;
        DiscountPlusPolicy discounts = null;
        PurchaseAndPolicy policies = null;
        return new ORM.Shops.Shop(id, name, description, founder, state,  products,
                DALUsersBaskets,  purchaseHistory,
                 shopAdministrators, discounts,  policies);
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
