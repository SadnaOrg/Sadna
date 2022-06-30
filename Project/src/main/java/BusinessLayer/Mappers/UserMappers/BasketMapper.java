package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Users.Basket;
import ORM.DAOs.Users.BasketDAO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BasketMapper {
    private BasketDAO dao = new BasketDAO();
    private Func<SubscribedUserMapper> subscribedUserMapper = () -> SubscribedUserMapper.getInstance();
    private Func<ShopMapper> shopMapper = () -> ShopMapper.getInstance();

    static private class BasketMapperHolder {

        static final BasketMapper mapper = new BasketMapper();
    }
    public static BasketMapper getInstance(){
        return BasketMapperHolder.mapper;
    }

    private BasketMapper() {

    }

    public ORM.Users.Basket toEntity(String username, Basket entity) {
        ORM.Users.Basket ormBasket = findORMById(username, entity.getShopid());
        if (ormBasket == null) {
            return new ORM.Users.Basket(shopMapper.run().findORMById(entity.getShopid()),
                    subscribedUserMapper.run().findORMById(username), entity.getProducts(), entity.getPrices(), entity.getCategories());
        }
        else {
            ormBasket.getProducts().clear();
            Map<Integer, Integer> products = new ConcurrentHashMap<>();
            for (int id : entity.getProducts().keySet()) {
                products.put(id, entity.getProducts().get(id));
            }
            ormBasket.getProducts().putAll(products);

            ormBasket.getPrices().clear();
            Map<Integer, Double> prices = new ConcurrentHashMap<>();
            for (int id : entity.getPrices().keySet()) {
                prices.put(id, entity.getPrices().get(id));
            }
            ormBasket.getPrices().putAll(prices);

            ormBasket.getCategories().clear();
            Map<Integer, String> categories = new ConcurrentHashMap<>();
            for (int id : entity.getCategories().keySet()) {
                categories.put(id, entity.getCategories().get(id));
            }
            ormBasket.getCategories().putAll(categories);
            return ormBasket;
        }
    }

    public Basket fromEntity(ORM.Users.Basket entity) {
        Basket basket = new Basket(entity.getShop().getId());
        basket.getProducts().clear();
        for (int id : entity.getProducts().keySet()) {
            basket.getProducts().put(id, entity.getProducts().get(id));
        }
        basket.getPrices().clear();
        for (int id : entity.getPrices().keySet()) {
            basket.getPrices().put(id, entity.getPrices().get(id));
        }
        basket.getCategories().clear();
        for (int id : entity.getCategories().keySet()) {
            basket.getCategories().put(id, entity.getCategories().get(id));
        }
        return basket;
    }

    public ORM.Users.Basket findORMById(String username, int shopID) {
        return dao.findById(new ORM.Users.Basket.BasketPKID(shopMapper.run().findORMById(shopID),
                subscribedUserMapper.run().findORMById(username)));
    }
}
