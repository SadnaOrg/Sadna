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
                    subscribedUserMapper.run().findORMById(entity.getFounder().getUser().getUserName()), true,
                    ORM.Shops.Shop.State.values()[entity.getState().ordinal()],
                    entity.getProducts().values().stream().map(product -> productMapper.run().toEntity(product)).toList(),
                    new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), new ConcurrentHashMap<>());

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
            //Map<SubscribedUser, Basket> usersBaskets = new ConcurrentHashMap<>();
            //entity.getUsersBaskets().keySet().stream()
            //        .peek(user -> usersBaskets.put(subscribedUserMapper.run().findORMById(user),
            //                basketMapper.run().toEntity(entity.getUsersBaskets().get(user))));
            //Map<SubscribedUser, PurchaseHistory> purchaseHistories = new ConcurrentHashMap<>();
            //entity.getPurchaseHistoryMap().keySet().stream()
            //        .peek(user -> purchaseHistories.put(subscribedUserMapper.run().findORMById(user),
            //                purchaseHistoryMapper.run().toEntity(entity.getPurchaseHistoryMap().get(user))));
            ormShop.setName(entity.getName());
            ormShop.setDescription(entity.getDescription());
            //for (String user : entity.getShopAdministratorsMap().keySet()) {
            //    SubscribedUser subscribedUser = subscribedUserMapper.run().findORMById(user);
            //    if (!ormShop.getShopAdministrators().containsKey(subscribedUser))
            //        ormShop.getShopAdministrators().put(subscribedUser,
            //                shopAdministratorMapper.run().toEntity(entity.getShopAdministratorsMap().get(user)));
            //}
            for (Product product : entity.getProducts().values()) {
                ORM.Shops.Product toEntity = productMapper.run().toEntity(product);
                if (!ormShop.getProducts().contains(toEntity))
                    ormShop.getProducts().add(toEntity);
            }
            //ormShop.setPurchaseHistory(purchaseHistories);
            ormShop.setState(ORM.Shops.Shop.State.values()[entity.getState().ordinal()]);
            for (BusinessLayer.Users.ShopAdministrator admin : entity.getFounder().getAppoints()) {
                ShopAdministrator toEntity = shopAdministratorMapper.run().toEntity(admin);
                if (!ormShop.getFounder().getAppoints().contains(toEntity))
                    ormShop.getFounder().getAppoints().add(toEntity);
            }
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
                (ConcurrentHashMap<Integer, Product>) entity.getProducts().stream().map(product -> productMapper.run().fromEntity(product))
                        .collect(Collectors.toConcurrentMap(Product::getID, Function.identity())),
                new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), new ConcurrentHashMap<>(),
                new DiscountPlusPolicy(), new PurchaseAndPolicy());
        founder.setShop(shop);
        shop.getShopAdministratorsMap().put(founder.getUserName(), founder);
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
