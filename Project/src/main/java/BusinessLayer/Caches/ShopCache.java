package BusinessLayer.Caches;

import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Shops.Shop;

import java.util.Collection;

public class ShopCache extends Cache<Integer, Shop> {
    public ShopCache(int maxSize) {
        super(maxSize);
    }

    @Override
    public Collection<Shop> findAll() {
        return ShopMapper.getInstance().findAll();
    }

    @Override
    public Shop remoteLookUp(Integer id) {
        return ShopMapper.getInstance().findById(id);
    }

    @Override
    public void remoteUpdate(Shop element) {
        ShopMapper.getInstance().save(element);
    }
}
