package BusinessLayer.Caches;

import BusinessLayer.Mappers.UserMappers.GuestMapper;
import BusinessLayer.Users.Guest;
import BusinessLayer.Users.User;

import java.util.Collection;

public class GuestCache extends Cache<String, Guest> {
    private GuestCache(int maxSize) {
        super(maxSize);
    }

    private static class GuestCacheHolder{
        static final GuestCache instance = new GuestCache(30);
    }

    public static GuestCache getInstance(){
        return GuestCacheHolder.instance;
    }

    @Override
    public Collection<Guest> findAll() {
        return GuestMapper.getInstance().findAll();
    }

    @Override
    public Guest remoteLookUp(String id) {
        return GuestMapper.getInstance().findById(id);
    }

    @Override
    public void remoteUpdate(Guest element) {
        GuestMapper.getInstance().update(element);
    }

    @Override
    public void remoteRemove(String id) {
        GuestMapper.getInstance().delete(id);
        Cacheable<String, Guest> cacheable = quickSearch(id);
        if(cacheable != null){
            cacheable.unMark();
            remove(id);
        }
    }

    @Override
    public void clear() {
        Collection<Guest> all = findAll();
        for (String username:
             all.stream().map(User::getUserName).toList()) {
                remoteRemove(username);
        }
        quickLookUp.clear();
        cache.clear();
    }
}
