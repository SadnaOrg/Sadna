package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import BusinessLayer.Mappers.PolicyAndPurchasesConverter;
import BusinessLayer.Mappers.UserMappers.BasketMapper;
import BusinessLayer.Mappers.UserMappers.ShopAdministratorMapper;
import BusinessLayer.Mappers.UserMappers.ShopOwnerMapper;
import BusinessLayer.Mappers.UserMappers.SubscribedUserMapper;
import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.Basket;
import ORM.DAOs.DBImpl;
import ORM.DAOs.Shops.ShopDAO;
import ORM.Shops.Discounts.DiscountPlusPolicy;
import ORM.Shops.Product;
import ORM.Shops.Purchases.PurchaseAndPolicy;
import ORM.Users.ShopAdministrator;
import ORM.Users.ShopOwner;
import ORM.Users.SubscribedUser;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ShopMapper implements DBImpl<Shop, Integer>, CastEntity<ORM.Shops.Shop, Shop> {

    private Func<SubscribedUserMapper> subscribedUserMapper = () -> SubscribedUserMapper.getInstance();
    private Func<ShopOwnerMapper> shopOwnerMapper = () -> ShopOwnerMapper.getInstance();
    private Func<ShopAdministratorMapper> shopAdministratorMapper = () -> ShopAdministratorMapper.getInstance();
    private final ShopDAO dao = new ShopDAO();
    private final PolicyAndPurchasesConverter converter = PolicyAndPurchasesConverter.getInstance();

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
        ShopOwner owner = (ShopOwner) subscribedUserMapper.run().findORMById(entity.getFounder().getUser().getUserName()).getAdministrators().stream().filter(shopAdministrator -> shopAdministrator.getShop().getId() == entity.getId()).toList().get(0);
        ORM.Shops.Shop shop = new ORM.Shops.Shop(entity.getId(), entity.getName(), entity.getDescription(),
                owner,
                ORM.Shops.Shop.State.values()[entity.getState().ordinal()], null, null,
                null, null,null,null);
        DiscountPlusPolicy DiscountPlusPolicy = (DiscountPlusPolicy) entity.getDiscounts().toEntity(converter, shop);
        PurchaseAndPolicy policy = (PurchaseAndPolicy) entity.getPurchasePolicy().toEntity(converter,shop);
        for (String admin : entity.getShopAdministratorsMap().keySet()) { // add admins
            if (admin != shop.getFounder().getUser().getUsername()) {
                ShopAdministrator ormAdmin = shopAdministratorMapper.run().toEntity(entity.getShopAdministrator(admin));
                ormAdmin.setUser(subscribedUserMapper.run().findORMById(admin));
                ormAdmin.setShop(shop);
                shop.getShopAdministrators().put(subscribedUserMapper.run().findORMById(admin), ormAdmin);
            }
        }
        Collection<Product> products = entity.getProducts().values().stream().map(product -> ProductMapper.getInstance().toEntity(product)).toList();
        ORM.Shops.PurchaseHistory purchaseHistory = PurchaseHistoryMapper.getInstance().toEntity(entity.getPurchaseHistory(), shop);
        return shop;
    }

    @Override
    public Shop fromEntity(ORM.Shops.Shop entity) {
        ConcurrentHashMap<Integer,BusinessLayer.Products.Product> productConcurrentHashMap = new ConcurrentHashMap<>();
        ConcurrentHashMap<String,BusinessLayer.Users.Basket> basketConcurrentHashMap = new ConcurrentHashMap<>();
        for (Product p:
             entity.getProducts()) { // build shop products
            BusinessLayer.Products.Product product = new BusinessLayer.Products.Product(p.getId(),p.getName(),p.getPrice(),p.getQuantity());
            product.setDescription(p.getDescription());
            product.setCategory(p.getCategory());
            product.setManufacturer(p.getManufacturer());
            productConcurrentHashMap.put(p.getId(),product);
        }
        for (SubscribedUser u:
             entity.getUsersBaskets().keySet()) {
            Basket basket = BasketMapper.getInstance().fromEntity(entity.getUsersBaskets().get(u));
            basketConcurrentHashMap.put(u.getUsername(), basket);
        }
        return new Shop(entity.getId(), entity.getName(), entity.getDescription(),
                Shop.State.values()[entity.getState().ordinal()], shopOwnerMapper.run().fromEntity(entity.getFounder()),
                productConcurrentHashMap, basketConcurrentHashMap, null, null);
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
