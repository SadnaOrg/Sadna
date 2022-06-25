package BusinessLayer.Caches;

import BusinessLayer.Mappers.UserMappers.SubscribedUserMapper;
import BusinessLayer.Users.SubscribedUser;

import java.util.Collection;

public class SubscribedUserCache extends Cache<String, SubscribedUser> {
    public SubscribedUserCache(int maxSize) {
        super(maxSize);
    }

    @Override
    public Collection<SubscribedUser> findAll() {
        return SubscribedUserMapper.getInstance().findAll();
    }

    @Override
    public SubscribedUser remoteLookUp(String id) {
        return SubscribedUserMapper.getInstance().findById(id);
    }

    @Override
    public void remoteUpdate(SubscribedUser element) {
        SubscribedUserMapper.getInstance().update(element);
    }
}
