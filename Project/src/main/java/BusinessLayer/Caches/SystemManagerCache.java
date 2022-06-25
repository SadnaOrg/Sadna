package BusinessLayer.Caches;

import BusinessLayer.Mappers.UserMappers.SystemManagerMapper;
import BusinessLayer.Users.SystemManager;

import java.util.Collection;

public class SystemManagerCache extends Cache<String, SystemManager> {
    public SystemManagerCache(int maxSize) {
        super(maxSize);
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
}
