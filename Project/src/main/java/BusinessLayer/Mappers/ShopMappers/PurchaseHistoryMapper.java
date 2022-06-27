package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import BusinessLayer.Mappers.UserMappers.SubscribedUserMapper;
import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.Basket;

import java.util.stream.Collectors;

public class PurchaseHistoryMapper  implements CastEntity<ORM.Shops.PurchaseHistory, PurchaseHistory> {

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
}
