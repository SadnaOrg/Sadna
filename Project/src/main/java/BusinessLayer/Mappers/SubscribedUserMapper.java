package BusinessLayer.Mappers;

import BusinessLayer.Users.SubscribedUser;
import com.SadnaORM.RepositoriesImpl.UserRepositoriesImpl.SubscribedUserRepositoryImpl;

public class SubscribedUserMapper implements MapperInterface<com.SadnaORM.Users.SubscribedUser, SubscribedUser, String>{
    private SubscribedUserMapper mapper = new SubscribedUserMapper();
    private SubscribedUserRepositoryImpl subscribedUserServiceImp = new SubscribedUserRepositoryImpl();

    public SubscribedUserMapper getInstance(){
        return this.mapper;
    }

    @Override
    public void save(SubscribedUser entity) {
        subscribedUserServiceImp.save(new com.SadnaORM.Users.SubscribedUser());
    }

    @Override
    public void delete(SubscribedUser entity) {
        subscribedUserServiceImp.delete(toEntity(entity));
    }

    @Override
    public com.SadnaORM.Users.SubscribedUser toEntity(SubscribedUser entity) {
        return new com.SadnaORM.Users.SubscribedUser();
    }

    @Override
    public SubscribedUser FromEntity(com.SadnaORM.Users.SubscribedUser entity) {
        return new SubscribedUser("123","123");
    }

    @Override
    public SubscribedUser findByID(String key) {
        return FromEntity(subscribedUserServiceImp.findById(key));
    }
}
