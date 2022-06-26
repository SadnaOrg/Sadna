package BusinessLayer.Caches;

import BusinessLayer.Mappers.UserMappers.SubscribedUserMapper;
import BusinessLayer.Mappers.UserMappers.SystemManagerMapper;
import BusinessLayer.Mappers.UserMappers.UserMapper;
import BusinessLayer.Users.SystemManager;
import BusinessLayer.Users.User;

import java.util.Collection;

public class UserCache extends Cache<String, User> {
    private UserCache(int maxSize) {
        super(maxSize);
    }

    private static class UserCacheHolder{
        static final UserCache instance = new UserCache(30);
    }

    public static UserCache getInstance(){
        return UserCacheHolder.instance;
    }

    @Override
    public Collection<User> findAll() {
        return UserMapper.getInstance().findAll();
    }

    @Override
    public User remoteLookUp(String id) {
        return UserMapper.getInstance().findById(id);
    }

    @Override
    public void remoteUpdate(User element) {
        UserMapper.getInstance().update(element);
    }

    @Override
    public void remoteRemove(String id) {
        UserMapper.getInstance().delete(id);
        Cacheable<String, User> cacheable = quickSearch(id);
        if(cacheable != null){
            cacheable.unMark();
            remove(id);
        }
    }
}
