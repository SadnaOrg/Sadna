package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import BusinessLayer.Mappers.UserMappers.SubscribedUserMapper;
import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.Basket;
import ORM.DAOs.DBImpl;
import ORM.DAOs.Shops.PurchaseHistoryDAO;
import ORM.DAOs.Shops.ShopDAO;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PurchaseHistoryMapper  implements DBImpl<PurchaseHistory, ORM.Shops.PurchaseHistory.PurchaseHistoryPKID>, CastEntity<ORM.Shops.PurchaseHistory, PurchaseHistory> {

    private final PurchaseHistoryDAO dao = new PurchaseHistoryDAO();

    private Func<PurchaseMapper> purchaseMapper = () -> PurchaseMapper.getInstance();

    private Func<SubscribedUserMapper> subscribedUserMapper = () -> SubscribedUserMapper.getInstance();

    private Func<ShopMapper> shopMapper = () -> ShopMapper.getInstance();




    static private class PurchaseHistoryMapperHolder {
        static final PurchaseHistoryMapper mapper = new PurchaseHistoryMapper();
    }

    public static PurchaseHistoryMapper getInstance(){
        return PurchaseHistoryMapperHolder.mapper;
    }

    private PurchaseHistoryMapper() {

    }
    @Override
    public ORM.Shops.PurchaseHistory toEntity(PurchaseHistory entity) {
        ORM.Shops.PurchaseHistory purchaseHistory = new ORM.Shops.PurchaseHistory(entity.getPast_purchases().stream()
                .map(purchase -> purchaseMapper.run().toEntity(purchase)).collect(Collectors.toList()));
        purchaseHistory.setUser(subscribedUserMapper.run().findORMById(entity.getUser()));
        purchaseHistory.setShop(shopMapper.run().findORMById(entity.getShop().getId()));

        return purchaseHistory;
    }

    @Override
    public PurchaseHistory fromEntity(ORM.Shops.PurchaseHistory entity) {
        return new PurchaseHistory(
                shopMapper.run().fromEntity(entity.getShop()),
                entity.getUser().getUsername(),
                entity.getPast_purchases().stream().map(purchase -> purchaseMapper.run().fromEntity(purchase)).collect(Collectors.toList()));
    }
    @Override
    public int save(PurchaseHistory purchaseHistory) {
        return dao.save(toEntity(purchaseHistory));
    }

    @Override
    public void update(PurchaseHistory purchaseHistory) {
        dao.update(toEntity((purchaseHistory)));
    }

    @Override
    public void delete(ORM.Shops.PurchaseHistory.PurchaseHistoryPKID integer) {
    }

    @Override
    public PurchaseHistory findById(ORM.Shops.PurchaseHistory.PurchaseHistoryPKID integer) {
        return fromEntity(dao.findById(integer));
    }

    public PurchaseHistory findByIds(int shopId, String user) {
        return fromEntity(dao.findById(new ORM.Shops.PurchaseHistory.PurchaseHistoryPKID(shopMapper.run().findORMById(shopId),subscribedUserMapper.run().findORMById(user))));
    }

    public ConcurrentHashMap<Integer,PurchaseHistory> findByUserName(String userName) {
        ConcurrentHashMap<Integer,PurchaseHistory> purchaseHistoryConcurrentHashMap = new ConcurrentHashMap<>();
        Collection<ORM.Shops.PurchaseHistory> ormPurchseHistories= dao.findAll().stream().filter(purchaseHistory1 -> purchaseHistory1.getUser().getUsername().equals(userName)).toList();
        for (ORM.Shops.PurchaseHistory ormHistory: ormPurchseHistories)
        {
            purchaseHistoryConcurrentHashMap.put(ormHistory.getShop().getId(),fromEntity(ormHistory));
        }
        return purchaseHistoryConcurrentHashMap;
    }

    public ConcurrentHashMap<String,PurchaseHistory> findByShopId(int shopId) {
        ConcurrentHashMap<String,PurchaseHistory> purchaseHistoryConcurrentHashMap = new ConcurrentHashMap<>();
        Collection<ORM.Shops.PurchaseHistory> ormPurchseHistories= dao.findAll().stream().filter(purchaseHistory1 -> purchaseHistory1.getShop().getId() == shopId).toList();
        for (ORM.Shops.PurchaseHistory ormHistory: ormPurchseHistories)
        {
            purchaseHistoryConcurrentHashMap.put(ormHistory.getUser().getUsername(),fromEntity(ormHistory));
        }
        return purchaseHistoryConcurrentHashMap;
    }

    @Override
    public Collection<PurchaseHistory> findAll() {
        return dao.findAll().stream().map(this::fromEntity).toList();
    }
}
