package BusinessLayer.Caches;

import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.SubscribedUser;

import java.util.Collection;

public class ShopCache extends Cache<Integer, Shop> {
    private ShopCache(int maxSize) {
        super(maxSize);
    }

    public void remoteUpdateByID(int shopID, SubscribedUser admin) {
        Shop shop = admin.getAdministrator(shopID).getShop();
        remoteUpdate(shop);
    }

    private static class ShopCacheHolder{
        static final ShopCache instance = new ShopCache(30);
    }

    public static ShopCache getInstance(){
        return ShopCacheHolder.instance;
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

    @Override
    public void remoteRemove(Integer id) {
        ShopMapper.getInstance().delete(id);
        Cacheable<Integer, Shop> cacheable = quickSearch(id);
        if(cacheable != null){
            cacheable.unMark();
            remove(id);
        }
    }

    @Override
    public void clear() {
        Collection<Shop> all = findAll();
        for (Integer id:
             all.stream().map(Shop::getId).toList()) {
            remoteRemove(id);
        }
        quickLookUp.clear();
        cache.clear();
    }
}
