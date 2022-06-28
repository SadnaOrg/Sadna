package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.ShopManager;
import ORM.Shops.Shop;
import ORM.Users.SubscribedUser;

import java.util.Map;

public class BasketMapper implements CastEntity<ORM.Users.Basket, Basket> {
    private ShopMapper shopMapper = ShopMapper.getInstance();
    private SubscribedUserMapper subscribedUserMapper = SubscribedUserMapper.getInstance();
    @Override
    public ORM.Users.Basket toEntity(Basket entity) {
        int shopID = entity.getShopid();
        ORM.Shops.Shop shop = shopMapper.toEntity(shopMapper.findById(shopID));
        ORM.Users.SubscribedUser user = subscribedUserMapper.toEntity(subscribedUserMapper.findById("")); // TODO: fix this
        Map<Integer,Integer> products = entity.getProducts();
        Map<Integer,Double> prices = entity.getPrices();
        Map<Integer, String> categories = entity.getCategories();
        return new ORM.Users.Basket(shop, user, products, prices, categories);
    }

    @Override
    public Basket fromEntity(ORM.Users.Basket entity) {
        return new Basket(entity.getShop().getId(), entity.getProducts(), entity.getPrices(), entity.getCategories());
    }

    static private class BasketMapperHolder {
        static final BasketMapper mapper = new BasketMapper();
    }

    public static BasketMapper getInstance(){
        return BasketMapperHolder.mapper;
    }

    private BasketMapper() {

    }
}
