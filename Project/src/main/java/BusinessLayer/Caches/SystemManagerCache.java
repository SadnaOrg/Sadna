package BusinessLayer.Caches;

import BusinessLayer.Mappers.UserMappers.SystemManagerMapper;
import BusinessLayer.Users.SystemManager;
import BusinessLayer.Users.User;

import java.util.Collection;

public class SystemManagerCache extends Cache<String, SystemManager> {
    private SystemManagerCache(int maxSize) {
        super(maxSize);
    }

    private static class SystemManagerCacheHolder{
        static final SystemManagerCache instance = new SystemManagerCache(30);
    }

    public static SystemManagerCache getInstance(){
        return SystemManagerCacheHolder.instance;
    }

    @Override
    public Collection<SystemManager> findAll() {
        return SystemManagerMapper.getInstance().findAll();
    }

    @Override
    public SystemManager remoteLookUp(String id) {
        return SystemManagerMapper.getInstance().findById(id);
    }

    @Override
    public void remoteUpdate(SystemManager element) {
        SystemManagerMapper.getInstance().update(element);
    }

    @Override
    public void remoteRemove(String id) {
        SystemManagerMapper.getInstance().delete(id);
        Cacheable<String, SystemManager> cacheable = quickSearch(id);
        if(cacheable != null){
            cacheable.unMark();
            remove(id);
        }
    }

    @Override
    public void clear() {
        Collection<SystemManager> all = findAll();
        for (String username:
             all.stream().map(User::getUserName).toList()) {
            remoteRemove(username);
        }
        quickLookUp.clear();
        cache.clear();
    }
}
