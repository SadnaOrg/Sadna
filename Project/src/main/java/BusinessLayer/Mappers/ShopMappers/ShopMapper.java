package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import BusinessLayer.Mappers.UserMappers.BasketMapper;
import BusinessLayer.Mappers.UserMappers.ShopAdministratorMapper;
import BusinessLayer.Mappers.UserMappers.ShopOwnerMapper;
import BusinessLayer.Mappers.UserMappers.SubscribedUserMapper;
import BusinessLayer.Products.Product;
import BusinessLayer.Shops.Polices.Discount.DiscountPlusPolicy;
import BusinessLayer.Shops.Polices.Purchase.PurchaseAndPolicy;
import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.ShopOwner;
import ORM.DAOs.DBImpl;
import ORM.DAOs.Shops.ShopDAO;
import ORM.Users.Basket;
import ORM.Users.ShopAdministrator;
import ORM.Users.SubscribedUser;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ShopMapper implements DBImpl<Shop, Integer>, CastEntity<ORM.Shops.Shop, Shop> {

    private Func<SubscribedUserMapper> subscribedUserMapper = () -> SubscribedUserMapper.getInstance();
    private Func<ProductMapper> productMapper = () -> ProductMapper.getInstance();
    private Func<ShopOwnerMapper> shopOwnerMapper = () -> ShopOwnerMapper.getInstance();
    private Func<ShopAdministratorMapper> shopAdministratorMapper = () -> ShopAdministratorMapper.getInstance();
    private Func<BasketMapper> basketMapper = () -> BasketMapper.getInstance();
    private Func<PurchaseHistoryMapper> purchaseHistoryMapper = () -> PurchaseHistoryMapper.getInstance();
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
        ORM.Shops.Shop ormShop = findORMById(entity.getId());
        if (ormShop == null) {
            ormShop = new ORM.Shops.Shop(entity.getName(), entity.getDescription(),
                    ORM.Shops.Shop.State.values()[entity.getState().ordinal()],
                    entity.getProducts().values().stream().map(product -> productMapper.run().toEntity(product)).toList(),
                    new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), new ConcurrentHashMap<>());

            ormShop.setFounder(shopOwnerMapper.run().toEntity(entity.getFounder()));
            ormShop.getFounder().setShop(ormShop);
            ormShop.getFounder().setUser(subscribedUserMapper.run().toEntity(entity.getFounder().getSubscribed()));
            ormShop.getShopAdministrators().put(ormShop.getFounder().getUser(), ormShop.getFounder());
            for (String admin : entity.getShopAdministratorsMap().keySet()) {
                if (admin != ormShop.getFounder().getUser().getUsername() && admin != entity.getFounder().getUserName()) {
                    ShopAdministrator ormAdmin = shopAdministratorMapper.run().toEntity(entity.getShopAdministrator(admin));
                    ormAdmin.setUser(subscribedUserMapper.run().findORMById(admin));
                    ormAdmin.setShop(ormShop);
                    ormShop.getShopAdministrators().put(subscribedUserMapper.run().findORMById(admin), ormAdmin);
                }
            }
        }
        else {
            ormShop.setName(entity.getName());
            ormShop.setDescription(entity.getDescription());
            //for (String user : entity.getShopAdministratorsMap().keySet()) {
            //    SubscribedUser subscribedUser = subscribedUserMapper.run().findORMById(user);
            //    if (!ormShop.getShopAdministrators().containsKey(subscribedUser))
            //        ormShop.getShopAdministrators().put(subscribedUser,
            //                shopAdministratorMapper.run().toEntity(entity.getShopAdministratorsMap().get(user)));
            //}
            ormShop.getProducts().clear();
            ormShop.getProducts().addAll(entity.getProducts().values().stream().map(
                    product -> productMapper.run().toEntity(product)
            ).toList());
            ormShop.getUsersBaskets().clear();
            for (String user : entity.getUsersBaskets().keySet())
                ormShop.getUsersBaskets().put(subscribedUserMapper.run().findORMById(user),
                        basketMapper.run().toEntity(user, entity.getUsersBaskets().get(user)));
            //ormShop.setPurchaseHistory(purchaseHistories);
            ormShop.setState(ORM.Shops.Shop.State.values()[entity.getState().ordinal()]);
            ormShop.getFounder().getAppoints().clear();
            ormShop.getFounder().getAppoints().addAll(entity.getFounder().getAppoints().stream().map(
                    appointee -> shopAdministratorMapper.run().toEntity(appointee)
            ).toList());
        }

        //for (ShopAdministrator admin : shop.getFounder().getUser().getAdministrators()){
        //    if (admin.getUser().getUsername() == shop.getFounder().getUser().getUsername()) {
        //        admin.setUser(shop.getFounder().getUser());
        //    }
        //    admin.setShop(shop);
        //};
        return ormShop;
    }

    @Override
    public Shop fromEntity(ORM.Shops.Shop entity) {
        ShopOwner founder = shopOwnerMapper.run().fromEntity(entity.getFounder());
        founder.setUser(subscribedUserMapper.run().fromEntity(entity.getFounder().getUser()));
        Shop shop = new Shop(entity.getId(), entity.getName(), entity.getDescription(),
                Shop.State.values()[entity.getState().ordinal()], founder,
                (ConcurrentHashMap<Integer, Product>) entity.getProducts().stream().map(product ->
                                productMapper.run().fromEntity(product))
                        .collect(Collectors.toConcurrentMap(Product::getID, Function.identity())),
                new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), new ConcurrentHashMap<>(),
                new DiscountPlusPolicy(), new PurchaseAndPolicy());
        founder.setShop(shop);
        shop.getShopAdministratorsMap().put(founder.getUserName(), founder);
        shop.getUsersBaskets().clear();
        for (SubscribedUser user : entity.getUsersBaskets().keySet()) {
            shop.getUsersBaskets().put(user.getUsername(),
                    basketMapper.run().fromEntity(entity.getUsersBaskets().get(user)));
        }
        founder.getSubscribed().addAdministrator(shop.getId(), founder);
        founder.getAppoints().stream().peek(admin -> admin.setShop(shop));
        return shop;
    }

    @Override
    public int save(Shop entity) {
        return dao.save(toEntity(entity));
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

    public ORM.Shops.Shop findORMById(Integer key) {
        return dao.findById(key);
    }

    public Collection<Shop> findByFounder(String username) {
        return dao.findByFounder(username).stream().map(this::fromEntity).toList();
    }

    @Override
    public Collection<Shop> findAll() {
        return dao.findAll().stream().map(this::fromEntity).toList();
    }
}
