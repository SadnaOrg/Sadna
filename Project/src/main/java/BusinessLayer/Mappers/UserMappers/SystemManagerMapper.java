package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Users.SystemManager;
import ORM.DAOs.DBImpl;

import java.util.Collection;

public class SystemManagerMapper implements DBImpl<SystemManager, String>, CastEntity<ORM.Users.SystemManager, SystemManager> {

    private static class SystemManagerMapperHolder{
        static  final SystemManagerMapper systemManagerMapper = new SystemManagerMapper();
    }

    private SystemManagerMapper(){

    }

    public static SystemManagerMapper getInstance(){
        return SystemManagerMapperHolder.systemManagerMapper;
    }
    @Override
    public ORM.Users.SystemManager toEntity(SystemManager entity) {
        return null;
    }

    @Override
    public SystemManager fromEntity(ORM.Users.SystemManager entity) {
        return null;
    }

    @Override
    public void save(SystemManager systemManager) {

    }

    @Override
    public void update(SystemManager systemManager) {

    }

    @Override
    public void delete(String s) {

    }

    @Override
    public SystemManager findById(String s) {
        return null;
    }

    @Override
    public Collection<SystemManager> findAll() {
        return null;
    }
}
