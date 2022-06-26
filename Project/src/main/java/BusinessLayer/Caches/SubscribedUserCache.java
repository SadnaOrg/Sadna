package BusinessLayer.Caches;

import BusinessLayer.Mappers.UserMappers.SubscribedUserMapper;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.SubscribedUser;

import java.util.Collection;

public class SubscribedUserCache extends Cache<String, SubscribedUser> {
    private SubscribedUserCache(int maxSize) {
        super(maxSize);
    }

    private static class SubscribedUserCacheHolder{
        static final SubscribedUserCache instance = new SubscribedUserCache(30);
    }

    public static SubscribedUserCache getInstance(){
        return SubscribedUserCacheHolder.instance;
    }

    @Override
    public Collection<SubscribedUser> findAll() {
        return SubscribedUserMapper.getInstance().findAll();
    }

    @Override
    public SubscribedUser remoteLookUp(String id) {
        SubscribedUser subscribedUser = SubscribedUserMapper.getInstance().findById(id);
        if(subscribedUser != null && ShopCache.getInstance().canPut(subscribedUser.getAdministrators().size())){
            Collection<Cacheable<Integer, Shop>> cacheables = subscribedUser.getAdministrators().stream().map(shopAdministrator -> new Cacheable<Integer, Shop>(shopAdministrator.getShopID(), shopAdministrator.getShop())).toList();
            ShopCache.getInstance().insertAll(cacheables);
        }
        return subscribedUser;
    }

    @Override
    public void remoteUpdate(SubscribedUser element) {
        SubscribedUserMapper.getInstance().update(element);
    }

    @Override
    public void remoteRemove(String id) {
        SubscribedUserMapper.getInstance().delete(id);
        Cacheable<String, SubscribedUser> cacheable = quickSearch(id);
        if(cacheable != null){
            cacheable.unMark();
            remove(id);
        }
    }
}
