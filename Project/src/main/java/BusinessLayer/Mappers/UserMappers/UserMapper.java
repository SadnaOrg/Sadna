package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Users.User;
import ORM.DAOs.DBImpl;

import java.util.Collection;

public class UserMapper implements DBImpl<User, String>, CastEntity<ORM.Users.User, User> {

    private static class UserMapperHolder{
        static final UserMapper instance = new UserMapper();
    }

    private UserMapper(){

    }

    public static UserMapper getInstance(){
        return UserMapperHolder.instance;
    }

    @Override
    public ORM.Users.User toEntity(User entity) {
        return null;
    }

    @Override
    public User fromEntity(ORM.Users.User entity) {
        return null;
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(String s) {

    }

    @Override
    public User findById(String s) {
        return null;
    }

    @Override
    public Collection<User> findAll() {
        return null;
    }
}
