package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.MapperInterface;
import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.SubscribedUser;
import com.SadnaORM.Shops.Shop;
import com.SadnaORM.UserImpl.UserObjects.BasketDTO;

public class BasketMapper {


    private final SubscribedUserMapper subscribedUserMapper = SubscribedUserMapper.getInstance();
    private final ShopMapper shopMapper = ShopMapper.getInstance();

    static private class BasketMapperHolder {
        static final BasketMapper mapper = new BasketMapper();
    }

    public static BasketMapper getInstance(){
        return BasketMapperHolder.mapper;
    }

    private BasketMapper() {

    }

    public BasketDTO toEntity(Basket entity, String subscribedUser) {
        return new BasketDTO(subscribedUser, entity.getShopid(), entity.getProducts());
    }

    public Basket FromEntity(BasketDTO entity) {
        return null;
    }
}
