package BusinessLayer.Caches;

import BusinessLayer.Mappers.UserMappers.UserMapper;
import BusinessLayer.Users.User;

import java.util.Collection;

public class UserCache extends Cache<String, User> {
    public UserCache(int maxSize) {
        super(maxSize);
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
}
