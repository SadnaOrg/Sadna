package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import ORM.DAOs.DBImpl;
import BusinessLayer.Users.SubscribedUser;
import ORM.DAOs.Users.SubscribedUserDAO;

import java.util.Collection;

public class SubscribedUserMapper implements DBImpl<SubscribedUser, String>, CastEntity<ORM.Users.SubscribedUser, SubscribedUser> {

    private SubscribedUserDAO dao = new SubscribedUserDAO();
    static private class SubscribedUserMapperHolder {
        static final SubscribedUserMapper mapper = new SubscribedUserMapper();
    }

    public static SubscribedUserMapper getInstance(){
        return SubscribedUserMapperHolder.mapper;
    }

    private SubscribedUserMapper() {

    }

    @Override
    public ORM.Users.SubscribedUser toEntity(SubscribedUser entity) {
        return new ORM.Users.SubscribedUser(entity.getUserName(), entity.getHashedPassword(), entity.isLoggedIn(), !entity.isRemoved(), null);
    }

    @Override
    public SubscribedUser fromEntity(ORM.Users.SubscribedUser entity) {
        return null;
    }

    @Override
    public void save(SubscribedUser entity) {
        dao.save(toEntity(entity));
    }

    @Override
    public void update(SubscribedUser entity) {
        dao.update(toEntity(entity));
    }

    @Override
    public void delete(String key) {
        dao.delete(key);
    }

    @Override
    public SubscribedUser findById(String key) {
        return fromEntity(dao.findById(key));
    }

    @Override
    public Collection<SubscribedUser> findAll() {
        return dao.findAll().stream().map(this::fromEntity).toList();
    }
}
